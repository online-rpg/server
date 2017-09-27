package com.vrpg.server.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static java.time.LocalDateTime.now;
import static java.time.ZoneOffset.UTC;
import static java.util.Collections.emptyList;
import static java.util.Date.from;

@Component
class TokenAuthenticationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TokenAuthenticationService.class);

    private static final String SECRET = "ThisIsASecret";
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String HEADER_STRING = "Authorization";

    private final ObjectMapper objectMapper;

    public TokenAuthenticationService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    void addAuthentication(HttpServletResponse res, String username) {
        LOGGER.trace("addAuthentication - {}, {}", res, username);
        Algorithm algorithm;
        try {
            algorithm = Algorithm.HMAC512(SECRET);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        String jwt = JWT.create()
                .withExpiresAt(from(now().plusDays(10).toInstant(UTC)))
                .withSubject(username)
                .sign(algorithm);

        LoginResponse loginResponse = LoginResponse.builder()
                .message("Logged in!")
                .accessToken(jwt)
                .build();

        try {
            objectMapper.writeValue(res.getWriter(), loginResponse);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    Authentication getAuthentication(HttpServletRequest request) {
        LOGGER.trace("getAuthentication - {}", request);
        String token = request.getHeader(HEADER_STRING);
        if (token != null) {
            if (!token.contains(TOKEN_PREFIX)) {
                return null;
            }
            Algorithm algorithm;
            try {
                algorithm = Algorithm.HMAC512(SECRET);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
            //TODO write more complex check!
            DecodedJWT decodedJWT = JWT.require(algorithm).build().verify(token.replace(TOKEN_PREFIX, ""));

            String user = decodedJWT.getSubject();

            return user != null ?
                    new UsernamePasswordAuthenticationToken(user, null, emptyList()) :
                    null;
        }
        return null;
    }
}
