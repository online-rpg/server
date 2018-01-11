package com.vrpg.server.security.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
class MongoUserStore implements UserStore {
    private static final Logger LOGGER = LoggerFactory.getLogger(MongoUserStore.class);

    private final UserRepository repository;

    public MongoUserStore(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User findUser(String email, String passToken) {
        LOGGER.trace("findUser - {}, {}", email, "[CONFIDENTIAL]");
        User user = repository.findOne(email);

        if (user != null && Objects.equals(user.getPassToken(), passToken)) {
            return user;
        }

        return null;
    }

    @Override
    public User registerUser(String email, String passToken) {
        LOGGER.trace("registerUser - {}, {}", email, "[CONFIDENTIAL]");
        return repository.save(User.builder()
                .email(email)
                .passToken(passToken)
                .build());
    }
}
