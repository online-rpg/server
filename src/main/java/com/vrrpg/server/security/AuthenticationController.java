package com.vrrpg.server.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping("sessions")
public class AuthenticationController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationController.class);

    @CrossOrigin("*")
    @RequestMapping(path = "/create", method = POST, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity createSession(@RequestParam("username") String username,
                                 @RequestParam("password") String password) throws Exception {
        LOGGER.trace("createSession");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(LoginResponse
                        .builder()
                        .accessToken("test")
                        .idToken("test2")
                        .build());
    }
}
