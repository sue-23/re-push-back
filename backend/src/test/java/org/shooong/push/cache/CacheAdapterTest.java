package org.shooong.push.cache;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.shooong.push.global.cache.CacheAdapter;
import org.shooong.push.global.cache.UserCacheKey;
import org.shooong.push.global.cache.UserCacheValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CacheAdapterTest {

    @Autowired
    private CacheAdapter cacheAdapter;

    @Test
    @DisplayName("캐시 데이터 조회")
    public void testGetOperation() {

        UserCacheKey key = UserCacheKey.from(1L);
        UserCacheValue value = UserCacheValue.of("수현", "aa@naver.com");
        cacheAdapter.put(key, value);

        UserCacheValue result = cacheAdapter.get(key);

        Assertions.assertEquals(value, result);
    }

    @Test
    @DisplayName("캐시 데이터 삭제")
    public void testDeleteOperation() {

        UserCacheKey key = UserCacheKey.from(1L);
        UserCacheValue value = UserCacheValue.of("수현", "aa@naver.com");
        cacheAdapter.put(key, value);

        cacheAdapter.delete(key);

        UserCacheValue result = cacheAdapter.get(key);
        Assertions.assertNull(result);
    }

}
