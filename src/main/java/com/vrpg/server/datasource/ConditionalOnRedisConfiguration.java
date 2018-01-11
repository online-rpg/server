package com.vrpg.server.datasource;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ConditionalOnProperty(name = {"redis.url"})
public @interface ConditionalOnRedisConfiguration {
}
