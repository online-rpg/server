package com.vrpg.server.security.model;

public interface TokenStore {

    boolean isTokenExists(String token);

    void saveToken(UserToken token);

    UserToken createToken(User user);
}
