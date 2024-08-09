package org.shooong.push.domain.user.users.service;

import org.shooong.push.global.exception.CustomJWTException;
import org.shooong.push.domain.user.users.repository.TokenRepository;
import org.shooong.push.global.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
@RequiredArgsConstructor
@Log4j2
public class RefreshTokenService {
    private final TokenRepository tokenRepository;
    private final JWTUtil jwtUtil;

    public Map<String,Object> refreshToken(String authHeader, String refreshToken){
        validateTokens(authHeader, refreshToken);

        String accessToken = authHeader.substring(7);

        if (!jwtUtil.checkExpiredToken(accessToken)) {
            log.info("Access token is still valid.");
            return Map.of("accessToken", accessToken, "refreshToken", refreshToken);
        }

        String email = jwtUtil.extractUsername(refreshToken);
        String storedRefreshToken = tokenRepository.getToken(email);

        validateStoredRefreshToken(email, storedRefreshToken);

        Map<String, Object> claims = jwtUtil.validateToken(refreshToken);

        return generateNewToken(claims, email, refreshToken);
    }


    private void validateTokens(String authHeader, String refreshToken) {
        if (refreshToken == null) {
            throw new CustomJWTException("NULL_REFRESH_TOKEN");
        }

        if (authHeader == null || authHeader.length() < 7) {
            throw new CustomJWTException("INVALID_AUTHORIZATION");
        }
    }

    private void validateStoredRefreshToken(String email, String refreshToken) {
        String storedRefreshToken = tokenRepository.getToken(email);

        if (storedRefreshToken == null || !storedRefreshToken.equals(refreshToken)) {
            throw new CustomJWTException("INVALID_REFRESH_TOKEN");
        }
    }

    private Map<String, Object> generateNewToken(Map<String, Object> claims, String email, String refreshToken) {
        String newAccessToken = jwtUtil.generateToken(claims, 30);
        String newRefreshToken = jwtUtil.checkTime((Integer) claims.get("exp"))
                ? jwtUtil.generateToken(claims, 60 * 24)
                : refreshToken;

        tokenRepository.updateRefreshToken(email, newRefreshToken, jwtUtil.extractExpiration(newRefreshToken));

        return Map.of("accessToken", newAccessToken, "refreshToken", newRefreshToken);
    }
}
