package com.vrpg.server.api.handler;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.vrpg.communication.model.networking.envelopes.AuthInfo;
import com.vrpg.communication.model.networking.envelopes.RequestEnvelope;
import com.vrpg.communication.model.networking.envelopes.ResponseEnvelope;
import com.vrpg.communication.model.networking.responses.Response;
import com.vrpg.communication.model.networking.responses.ResponseType;
import com.vrpg.server.api.exception.InvalidTokenException;
import com.vrpg.server.security.model.TokenStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract class AbstractRestMessageHandler<REQUEST extends Message, RESPONSE extends Message> implements RestMessageHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractRestMessageHandler.class);

    private final TokenStore tokenStore;

    AbstractRestMessageHandler(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    @Override
    public final ResponseEnvelope handle(RequestEnvelope message) {
        LOGGER.trace("handle - {}", message);

        auth(message.getAuthInfo());

        REQUEST request;
        try {
            request = parse(message.getRequest().getRequestMessage());
        } catch (InvalidProtocolBufferException e) {
            //TODO send proper error code
            throw new RuntimeException(e);
        }

        return ResponseEnvelope.newBuilder()
                .setResponse(Response.newBuilder()
                        .setResponseType(getResponseType())
                        .setResponseMessage(handle(request).toByteString())
                        .build())
                .build();
    }

    void auth(AuthInfo authInfo) {
        LOGGER.trace("auth - {}", authInfo);

        if (!tokenStore.isTokenExists(authInfo.getJwtToken())) {
            throw new InvalidTokenException("Security token is not valid!");
        }
    }

    abstract RESPONSE handle(REQUEST request);

    abstract REQUEST parse(ByteString bytes) throws InvalidProtocolBufferException;

    abstract ResponseType getResponseType();
}
