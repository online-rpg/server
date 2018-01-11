package com.vrpg.server.api;

import com.vrpg.communication.model.networking.envelopes.RequestEnvelope;
import com.vrpg.communication.model.networking.envelopes.ResponseEnvelope;
import com.vrpg.communication.model.networking.requests.Request;
import com.vrpg.communication.model.networking.requests.RequestType;
import com.vrpg.server.api.exception.BadRequestException;
import com.vrpg.server.api.handler.RestMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RestHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestHandler.class);

    private final Map<RequestType, RestMessageHandler> handlers;

    RestHandler(Collection<RestMessageHandler> messageHandlers) {
        handlers = messageHandlers.stream()
                .collect(Collectors.toMap(RestMessageHandler::getRequestType, o -> o));
    }

    public ResponseEnvelope handle(RequestEnvelope requestEnvelope) {
        LOGGER.trace("handle - {}", requestEnvelope);

        Request request = requestEnvelope.getRequest();
        if (request == null) {
            throw new BadRequestException("Bad request! Request is empty!");
        }

        RequestType requestType = request.getRequestType();
        RestMessageHandler handler = handlers.get(requestType);
        if (handler == null) {
            String message = MessageFormat.format("No handler for request type - {0}", requestType);
            LOGGER.warn(message);
            return ResponseEnvelope.newBuilder()
                    .setMessage(message)
                    .build();
        }

        return handler.handle(requestEnvelope);
    }
}
