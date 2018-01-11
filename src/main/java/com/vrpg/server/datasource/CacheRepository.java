package com.vrpg.server.datasource;

import java.util.concurrent.TimeUnit;

public interface CacheRepository<T> extends Repository<T> {

    void save(String key, T model, long expiration, TimeUnit expirationUnit);
}
