package org.shooong.push.global.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class UserCacheValueSerializer implements RedisSerializer<UserCacheValue> {
    public static final ObjectMapper objectMapper = new ObjectMapper();
    private final Charset UTF_8 = StandardCharsets.UTF_8;

    @Override
    public byte[] serialize(UserCacheValue userCacheValue) throws SerializationException {
        if (Objects.isNull(userCacheValue))
            return null;

        try {
            String json = objectMapper.writeValueAsString(userCacheValue);
            return json.getBytes(UTF_8);
        } catch (JsonProcessingException e) {
            throw new SerializationException("json serialize error", e);
        }
    }

    @Override
    public UserCacheValue deserialize(byte[] bytes) throws SerializationException {
        if (Objects.isNull(bytes))
            return null;

        try {
            return objectMapper.readValue(new String(bytes, UTF_8), UserCacheValue.class);
        } catch (JsonProcessingException e) {
            throw new SerializationException("json deserialize error", e);
        }
    }
}


