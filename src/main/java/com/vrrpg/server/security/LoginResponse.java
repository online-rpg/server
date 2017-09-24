package com.vrrpg.server.security;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
class LoginResponse {
    private String idToken;
    private String accessToken;
}
