package com.vrpg.server.api.handler;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.vrpg.communication.model.networking.requests.RequestType;
import com.vrpg.communication.model.networking.requests.messages.RegisterRequestMessage;
import com.vrpg.communication.model.networking.responses.ResponseType;
import com.vrpg.communication.model.networking.responses.messages.RegisterResponseMessage;
import com.vrpg.server.api.exception.AlreadyExistingUserException;
import com.vrpg.server.api.exception.InvalidCredentialsException;
import com.vrpg.server.security.model.TokenStore;
import com.vrpg.server.security.model.User;
import com.vrpg.server.security.model.UserStore;
import com.vrpg.server.security.model.UserToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
class RegisterRestMessageHandler extends AbstractRestMessageHandler<RegisterRequestMessage, RegisterResponseMessage> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterRestMessageHandler.class);

    private final UserStore userStore;
    private final TokenStore tokenStore;

    RegisterRestMessageHandler(UserStore userStore, TokenStore tokenStore) {
        this.userStore = userStore;
        this.tokenStore = tokenStore;
    }

    @Override
    RegisterResponseMessage handle(RegisterRequestMessage request) {
        LOGGER.trace("handle - {}", request);

        if (request.getEmail() == null || request.getPassToken() == null) {
            throw new InvalidCredentialsException();
        }

        User user = userStore.findUser(request.getEmail(), request.getPassToken());

        if (user == null) {
            UserToken token = tokenStore.createToken(userStore.registerUser(request.getEmail(), request.getPassToken()));

            return RegisterResponseMessage.newBuilder()
                    .setMessage("Registration successful!")
                    .setAccessToken(token.getAccessToken())
                    .setRefreshToken(token.getRefreshToken())
                    .build();
        } else {
            throw new AlreadyExistingUserException();
        }
    }

    @Override
    ResponseType getResponseType() {
        LOGGER.trace("getResponseType");
        return ResponseType.REGISTER_RESPONSE;
    }

    @Override
    RegisterRequestMessage parse(ByteString bytes) throws InvalidProtocolBufferException {
        LOGGER.trace("parse - {}", bytes);
        return RegisterRequestMessage.parseFrom(bytes);
    }

    @Override
    public RequestType getRequestType() {
        LOGGER.trace("getRequestType");
        return RequestType.REGISTER_REQUEST;
    }
}
