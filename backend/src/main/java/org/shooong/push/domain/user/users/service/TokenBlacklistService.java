package org.shooong.push.domain.user.users.service;

import org.shooong.push.domain.user.users.repository.TokenRepository;
import org.shooong.push.global.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
public class TokenBlacklistService {

    private final TokenRepository tokenRepository;
    private final JWTUtil jwtUtil;

    @Transactional
    public void addBlackList(String authHeader) {
        String accessToken = authHeader.substring(7);
        Long expiration = jwtUtil.extractExpiration(accessToken) - (System.currentTimeMillis() / 1000);
        String email = jwtUtil.extractUsername(accessToken);

        tokenRepository.saveBlackList(accessToken, expiration);
        tokenRepository.removeRefreshToken(email);
    }
}