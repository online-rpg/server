package com.vrpg.server.security;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LoginResponse {
    private String message;
    private String idToken;
    private String accessToken;
}
