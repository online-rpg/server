package com.vrpg.server.security.model;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.vrpg.server.datasource.CacheRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import static java.time.LocalDateTime.now;
import static java.time.ZoneOffset.UTC;
import static java.util.Date.from;

class CacheTokenStore implements TokenStore {
    private static final Logger LOGGER = LoggerFactory.getLogger(CacheTokenStore.class);

    private static final String SECRET = "ThisIsASecret";

    private final CacheRepository<UserToken> repository;
    private final Algorithm algorithm;

    CacheTokenStore(CacheRepository<UserToken> repository) {
        this.repository = repository;

        try {
            algorithm = Algorithm.HMAC512(SECRET);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isTokenExists(String token) {
        LOGGER.trace("isTokenExists - {}", token);

        if (token == null || token.isEmpty()) {
            return false;
        }

        DecodedJWT decodedJWT = JWT.require(algorithm).build().verify(token);

        return repository.findOne(decodedJWT.getSubject()) != null;
    }

    @Override
    public void saveToken(UserToken token) {
        LOGGER.trace("saveToken - {}", token);
        repository.save(token.getId(), token, 1, TimeUnit.DAYS);
    }

    @Override
    public UserToken createToken(User user) {
        LOGGER.trace("createToken - {}", user);
        LocalDateTime now = now();

        String accessToken = JWT.create()
                .withExpiresAt(from(now.plusMinutes(10).toInstant(UTC)))
                .withIssuedAt(from(now.toInstant(UTC)))
                .withSubject(user.getEmail())
                .sign(algorithm);

        String refreshToken = JWT.create()
                .withIssuedAt(from(now.toInstant(UTC)))
                .withSubject(user.getEmail())
                .sign(algorithm);

        UserToken userToken = UserToken.builder()
                .id(user.getEmail())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        saveToken(userToken);

        return userToken;
    }
}
