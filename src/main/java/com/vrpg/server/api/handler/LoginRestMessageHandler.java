package com.vrpg.server.api.handler;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.vrpg.communication.model.networking.requests.RequestType;
import com.vrpg.communication.model.networking.requests.messages.LoginRequestMessage;
import com.vrpg.communication.model.networking.responses.ResponseType;
import com.vrpg.communication.model.networking.responses.messages.LoginResponseMessage;
import com.vrpg.server.api.exception.InvalidCredentialsException;
import com.vrpg.server.security.model.TokenStore;
import com.vrpg.server.security.model.User;
import com.vrpg.server.security.model.UserStore;
import com.vrpg.server.security.model.UserToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
class LoginRestMessageHandler extends AbstractRestMessageHandler<LoginRequestMessage, LoginResponseMessage> {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginRestMessageHandler.class);

    private final UserStore userStore;
    private final TokenStore tokenStore;

    LoginRestMessageHandler(UserStore userStore, TokenStore tokenStore) {
        this.userStore = userStore;
        this.tokenStore = tokenStore;
    }

    @Override
    LoginResponseMessage handle(LoginRequestMessage loginRequestMessage) {
        LOGGER.trace("handle - {}", loginRequestMessage);

        User user = userStore.findUser(loginRequestMessage.getEmail(), loginRequestMessage.getPassToken());

        if (user == null) {
            throw new InvalidCredentialsException();
        } else {
            UserToken token = tokenStore.createToken(user);

            return LoginResponseMessage.newBuilder()
                    .setMessage("Login successful!")
                    .setAccessToken(token.getAccessToken())
                    .setRefreshToken(token.getRefreshToken())
                    .build();
        }
    }

    @Override
    ResponseType getResponseType() {
        LOGGER.trace("getResponseType");
        return ResponseType.LOGIN_RESPONSE;
    }

    @Override
    LoginRequestMessage parse(ByteString bytes) throws InvalidProtocolBufferException {
        LOGGER.trace("parse - {}", bytes);
        return LoginRequestMessage.parseFrom(bytes);
    }

    @Override
    public RequestType getRequestType() {
        LOGGER.trace("getRequestType");
        return RequestType.LOGIN_REQUEST;
    }
}
