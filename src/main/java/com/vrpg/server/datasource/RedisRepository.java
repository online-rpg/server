package com.vrpg.server.datasource;

import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class RedisRepository<T> extends KeyBasedRedisRepository<T> {
    private final Function<T, String> keyFunction;

    public RedisRepository(String prefix, RedisTemplate<String, T> template, Function<T, String> keyFunction) {
        super(prefix, template);
        this.keyFunction = keyFunction;
    }

    public void save(T model) {
        if (model == null) {
            return;
        }
        save(keyFunction.apply(model), model);
    }

    public void save(T model, long expiration, TimeUnit expirationUnit) {
        if (model == null) {
            return;
        }
        save(keyFunction.apply(model), model, expiration, expirationUnit);
    }
}
