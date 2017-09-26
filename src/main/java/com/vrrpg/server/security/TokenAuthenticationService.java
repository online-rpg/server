package com.vrrpg.server.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

import static java.time.LocalDateTime.now;
import static java.time.ZoneOffset.UTC;
import static java.util.Collections.emptyList;
import static java.util.Date.from;

class TokenAuthenticationService {
    private static final String SECRET = "ThisIsASecret";
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String HEADER_STRING = "Authorization";

    static void addAuthentication(HttpServletResponse res, String username) {
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

        res.addHeader(HEADER_STRING, TOKEN_PREFIX + jwt);
    }

    static Authentication getAuthentication(HttpServletRequest request) {
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

            // parse the token.
            String user = decodedJWT.getSubject();

            return user != null ?
                    new UsernamePasswordAuthenticationToken(user, null, emptyList()) :
                    null;
        }
        return null;
    }
}