package com.vrpg.server.datasource;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.net.URI;
import java.net.URISyntaxException;

@Configuration
@ConditionalOnClass({JedisConnectionFactory.class, Jackson2JsonRedisSerializer.class, ObjectMapper.class})
public class RedisConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisConfiguration.class);

    public static <T> RedisTemplate<String, T> createJsonRedisTemplate(RedisConnectionFactory connectionFactory, Class<T> clazz, ObjectMapper objectMapper) {
        RedisTemplate<String, T> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());

        Jackson2JsonRedisSerializer<T> serializer = new Jackson2JsonRedisSerializer<>(clazz);
        serializer.setObjectMapper(objectMapper);
        template.setValueSerializer(serializer);
        return template;
    }

    @Bean
    @ConditionalOnRedisConfiguration
    RedisConnectionFactory jedisConnectionFactory(@Value("${redis.url}") String redisUrl) {
        URI redisUri;
        try {
            redisUri = new URI(redisUrl);
        } catch (URISyntaxException e) {
            LOGGER.error("Couldn't configure redis with url: {}", redisUrl, e);
            return null;
        }

        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(5);
        jedisPoolConfig.setMinIdle(1);
        jedisPoolConfig.setMaxTotal(10);
        jedisPoolConfig.setTestOnBorrow(true);
        jedisPoolConfig.setTestOnReturn(true);
        jedisPoolConfig.setTestWhileIdle(true);

        JedisConnectionFactory connectionFactory = new JedisConnectionFactory(jedisPoolConfig);
        connectionFactory.setHostName(redisUri.getHost());
        connectionFactory.setPort(redisUri.getPort());
        connectionFactory.setPassword(redisUri.getUserInfo().split(":", 2)[1]);

        return connectionFactory;
    }
}
