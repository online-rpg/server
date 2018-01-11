package com.vrpg.server.security.model;

public interface UserStore {

    User findUser(String email, String passToken);

    User registerUser(String email, String passToken);
}
