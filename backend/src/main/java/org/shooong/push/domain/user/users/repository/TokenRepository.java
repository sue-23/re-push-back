package org.shooong.push.domain.user.users.repository;

import org.shooong.push.global.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
public class TokenRepository {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    private JWTUtil jwtUtil;

    @Autowired
    public TokenRepository(RedisTemplate<String, String> redisTemplate, JWTUtil jwtUtil) {
        this.redisTemplate = redisTemplate;
        this.jwtUtil = jwtUtil;
    }


    /**
     * 토큰 저장
     * @param email key
     * @param token value
     * @param expiration ttl
     */
    public void saveToken(String email, String token, Long expiration) {
        redisTemplate.opsForValue().set(
                email, token, jwtUtil.extractExpiration(token), TimeUnit.SECONDS
        );
    }

    /**
     * 사용자 email로 저장된 토큰 조회
     * @param email key
     * @return token - value
     */
    public String getToken(String email) {
        return redisTemplate.opsForValue().get(email);
    }

    /**
     * newRefreshToken 저장
     */
    public void updateRefreshToken(String email, String newRefreshToken, Long expiration) {
        redisTemplate.delete(email);

        redisTemplate.opsForValue().set(
                email, newRefreshToken, expiration, TimeUnit.SECONDS
        );
    }

    public void saveBlackList(String token, Long expiration) {
        redisTemplate.opsForValue().set(
                token, String.valueOf(true), expiration, TimeUnit.SECONDS
        );
    }

    /**
     * 요청에서 받은 accessToken이 블랙리스트 등록되어 있는지 확인
     */
    public boolean isBlacklist(String accessToken) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(accessToken));
    }

    /**
     * 저장된 refreshToken 삭제
     */
    public void removeRefreshToken(String email) {
        redisTemplate.delete(email);
    }
}
