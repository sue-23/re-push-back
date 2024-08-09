package org.shooong.push.global.cache;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class CacheAdapter {

    private final RedisTemplate<UserCacheKey, UserCacheValue> userCacheRedisTemplate;
    private final ValueOperations<UserCacheKey, UserCacheValue> userCacheOperations;

    public CacheAdapter(RedisTemplate<UserCacheKey, UserCacheValue> userCacheRedisTemplate) {
        this.userCacheRedisTemplate = userCacheRedisTemplate;
        this.userCacheOperations = userCacheRedisTemplate.opsForValue();
    }

    public void put(UserCacheKey key, UserCacheValue value) {
        userCacheOperations.set(key, value, Duration.ofSeconds(24 * 60 * 60));
    }

    public UserCacheValue get(UserCacheKey key) {
        return userCacheOperations.get(key);
    }

    public void delete(UserCacheKey key) {
        userCacheRedisTemplate.delete(key);
    }
}
