package org.shooong.push.global.cache;

import java.util.Objects;

public class UserCacheKey {

    private static final String PREFIX = "USER::";

    private Long userId;

    private UserCacheKey(Long userId) {
        if (Objects.isNull(userId))
            throw new IllegalArgumentException("userId cannot be null");

        this.userId = userId;
    }

    public static UserCacheKey from(Long userId) {
        return new UserCacheKey(userId);
    }

    public static UserCacheKey fromString(String key) {
        String idToken = key.substring(0, PREFIX.length());
        Long userId = Long.valueOf(idToken);

        return UserCacheKey.from(userId);
    }

    @Override
    public String toString() {
        return PREFIX + userId;
    }
}
