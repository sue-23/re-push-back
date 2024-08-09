package org.shooong.push.global.util;

import org.shooong.push.global.exception.CustomJWTException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;

@Component
@Log4j2
public class JWTUtil {

    // TODO: 네이버클라우드플랫폼 KMS로 변경
    private final SecretKey key;

    public JWTUtil(@Value("${jwt.secret}") String secretKey) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * JWT 생성
     * @param valueMap claims
     * @param min 만료 시간
     */
    public String generateToken(Map<String, Object> valueMap, int min) {
        String jwtStr = null;

        try {
           jwtStr = Jwts.builder()
                    .setHeader(Map.of("typ", "JWT"))
                    .setClaims(valueMap)
                    .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                    .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(min).toInstant()))
                    .signWith(key, SignatureAlgorithm.HS256)
                    .compact();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        return jwtStr;
    }

    /**
     * JWT 유효성 검증 및 claims 반환
     */
    public Map<String, Object> validateToken(String token) {
        Map<String, Object> claims;

        try {
            claims = extractClaims(token);
        } catch (MalformedJwtException e) {
            throw new CustomJWTException("MalFormed");
        } catch (ExpiredJwtException e) {
            throw new CustomJWTException("Expired");
        } catch (InvalidClaimException e) {
            throw new CustomJWTException("Invalid");
        } catch (JwtException e) {
            throw new CustomJWTException("JWTError");
        } catch (Exception e) {
            throw new CustomJWTException("Error");
        }

        return claims;
    }

    /**
     * 토큰에서 클레임 추출
     */
    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 토큰 만료 시간 추출
     */
    public Long extractExpiration(String token) {
        Claims claims = extractClaims(token);

        return claims.getExpiration().getTime() / 1000;
    }

    /**
     * 토큰에서 username 추출
     */
    public String extractUsername(String token) {
        return extractClaims(token).get("email", String.class);
    }

    /**
     * 토큰 잔여 유효시간 검사
     */
    public boolean checkTime(Integer exp) {

        Date expDate = new Date((long) exp * 1000);

        long gap = expDate.getTime() - System.currentTimeMillis();

        long leftMin = gap / (1000 * 60);

        return leftMin < 60;
    }

    /**
     * 토큰 유효성 검사 (만료 여부)
     */
    public boolean checkExpiredToken(String token) {
        try {
            validateToken(token);
        } catch (CustomJWTException e) {
            if (e.getMessage().equals("Expired")) { return true; }
        }

        return false;
    }
}
