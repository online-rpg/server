package com.vrpg.server.datasource;

public interface Repository<T> {

    void save(String key, T model);

    T findOne(String key);

    void deleteOne(String key);
}
