package org.shooong.push.global.config;

import org.shooong.push.global.cache.UserCacheKey;
import org.shooong.push.global.cache.UserCacheKeySerializer;
import org.shooong.push.global.cache.UserCacheValue;
import org.shooong.push.global.cache.UserCacheValueSerializer;
import io.lettuce.core.ClientOptions;
import io.lettuce.core.SocketOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;

@Configuration
public class CacheConfig {

    @Value(("${spring.data.redis.url}"))
    private String redisUrl;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Value("${spring.data.redis.password}")
    private String redisPassword;


    @Bean
    public RedisConnectionFactory cacheRedisConnectionFactory() {
        RedisStandaloneConfiguration standaloneConfiguration = new RedisStandaloneConfiguration
                (redisUrl, redisPort);
        standaloneConfiguration.setDatabase(0);
        standaloneConfiguration.setPassword(redisPassword);

        final SocketOptions socketOptions = SocketOptions.builder()
                .connectTimeout(Duration.ofSeconds(10)).build();
        final ClientOptions clientOptions = ClientOptions.builder()
                .socketOptions(socketOptions).build();

        LettuceClientConfiguration lettuceClientConfiguration = LettuceClientConfiguration.builder()
                .clientOptions(clientOptions)
                .commandTimeout(Duration.ofSeconds(5))
                .shutdownTimeout(Duration.ZERO)
                .build();

        return new LettuceConnectionFactory(standaloneConfiguration, lettuceClientConfiguration);

    }

    @Bean(name = "userCacheRedisTemplate")
    public RedisTemplate<UserCacheKey, UserCacheValue> userCacheRedisTemplate() {
        RedisTemplate<UserCacheKey, UserCacheValue> refreshTokenRedisTemplate = new RedisTemplate<>();
        refreshTokenRedisTemplate.setConnectionFactory(cacheRedisConnectionFactory());
        refreshTokenRedisTemplate.setKeySerializer(new UserCacheKeySerializer());
        refreshTokenRedisTemplate.setValueSerializer(new UserCacheValueSerializer());

        return refreshTokenRedisTemplate;
    }
}


