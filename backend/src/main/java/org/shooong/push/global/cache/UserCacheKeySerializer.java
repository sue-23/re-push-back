package org.shooong.push.global.cache;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class UserCacheKeySerializer implements RedisSerializer<UserCacheKey> {
    private final Charset UTF_8 = StandardCharsets.UTF_8;

    @Override
    public byte[] serialize(UserCacheKey userCacheKey) throws SerializationException {
        if (Objects.isNull(userCacheKey))
            throw new SerializationException("UserCacheKey is null");

        return userCacheKey.toString().getBytes(UTF_8);
    }

    @Override
    public UserCacheKey deserialize(byte[] bytes) throws SerializationException {
        if (Objects.isNull(bytes))
            throw new SerializationException("bytes is null");

        return UserCacheKey.fromString(new String(bytes, UTF_8));
    }
}



