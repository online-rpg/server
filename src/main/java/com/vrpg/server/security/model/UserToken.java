package com.vrpg.server.security.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserToken {
    private final String id;
    private final String accessToken;
    private final String refreshToken;
}
