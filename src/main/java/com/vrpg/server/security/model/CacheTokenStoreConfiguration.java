package com.vrpg.server.security.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vrpg.server.datasource.CacheRepository;
import com.vrpg.server.datasource.ConditionalOnRedisConfiguration;
import com.vrpg.server.datasource.RedisRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.vrpg.server.datasource.RedisConfiguration.createJsonRedisTemplate;

@Configuration
class CacheTokenStoreConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(CacheTokenStoreConfiguration.class);

    @Bean
    @ConditionalOnRedisConfiguration
    RedisTemplate<String, UserToken> userTokenRedisTemplate(RedisConnectionFactory connectionFactory, ObjectMapper objectMapper) {
        LOGGER.trace("userTokenRedisTemplate - {}, {}", connectionFactory, objectMapper);
        return createJsonRedisTemplate(connectionFactory, UserToken.class, objectMapper);
    }

    @Bean
    @ConditionalOnRedisConfiguration
    TokenStore createRedisTokenStore(RedisTemplate<String, UserToken> userTokenRedisTemplate) {
        LOGGER.trace("createRedisTokenStore - {}", userTokenRedisTemplate);
        return new CacheTokenStore(new RedisRepository<>("token_store:", userTokenRedisTemplate, UserToken::getId));
    }

    @Bean
    @ConditionalOnMissingBean
    TokenStore createOnMemoryTokenStore() {
        LOGGER.trace("createOnMemoryTokenStore");
        LOGGER.info("Using on-memory token store!");
        return new CacheTokenStore(new CacheRepository<UserToken>() {
            private final Map<String, UserToken> store = new HashMap<>();

            @Override
            public void save(String key, UserToken model, long expiration, TimeUnit expirationUnit) {
                LOGGER.warn("Caching is not supported with on-memory token store!");
                save(key, model);
            }

            @Override
            public void save(String key, UserToken model) {
                store.put(key, model);
            }

            @Override
            public UserToken findOne(String key) {
                return store.get(key);
            }

            @Override
            public void deleteOne(String key) {
                store.remove(key);
            }
        });
    }
}
