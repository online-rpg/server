package com.vrpg.server.datasource;

import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

public class KeyBasedRedisRepository<T> implements CacheRepository<T> {
    private final String prefix;
    private final RedisTemplate<String, T> template;

    public KeyBasedRedisRepository(String prefix, RedisTemplate<String, T> template) {
        this.prefix = prefix;
        this.template = template;
    }

    public void save(String key, T model) {
        if (model == null) {
            return;
        }

        template.opsForValue().set(prefix + key, model);
    }

    public void save(String key, T model, long expiration, TimeUnit expirationUnit) {
        if (model == null) {
            return;
        }

        template.opsForValue().set(prefix + key, model, expiration, expirationUnit);
    }

    public T findOne(String key) {
        if (key == null) {
            return null;
        }

        return template.opsForValue().get(prefix + key);
    }

    @Override
    public void deleteOne(String key) {
        if (findOne(key) != null) {
            template.delete(prefix + key);
        }
    }
}
