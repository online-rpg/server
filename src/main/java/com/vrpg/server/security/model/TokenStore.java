package com.vrpg.server.security.model;

public interface TokenStore {

    boolean isTokenExists(String id);

    void saveToken(UserToken token);

    UserToken createToken(User user);
}
