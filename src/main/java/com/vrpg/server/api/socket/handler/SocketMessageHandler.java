package com.vrpg.server.api.socket.handler;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;
import com.vrpg.communication.model.networking.envelopes.SocketEnvelope;
import com.vrpg.communication.model.networking.socketmessages.SocketMessageType;
import com.vrpg.server.api.socket.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Objects;

public abstract class SocketMessageHandler<T extends Message> {
    private static final Logger LOGGER = LoggerFactory.getLogger(SocketMessageHandler.class);

    private final SessionManager sessionManager;

    SocketMessageHandler(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public final void handleMessage(WebSocketSession session, SocketEnvelope socketEnvelope) {
        LOGGER.trace("handleMessage - {}, {}", session, socketEnvelope);
        try {
            T message = parse(socketEnvelope.getSocketMessage().getMessage());
            handleMessage(session, message);
            broadcast(socketEnvelope, session);
        } catch (InvalidProtocolBufferException e) {
            LOGGER.warn(e.getMessage(), e);
        }
    }

    void handleMessage(WebSocketSession session, T message) {
        LOGGER.trace("handleMessage - {}, {}", session, message);
    }

    abstract T parse(ByteString bytes) throws InvalidProtocolBufferException;

    public abstract SocketMessageType getMessageType();

    private void broadcast(SocketEnvelope envelope, WebSocketSession currentSession) {
        try {
            TextMessage textMessage = new TextMessage(JsonFormat.printer().print(envelope));
            sessionManager.getAliveSessions()
                    .keySet()
                    .stream()
                    .filter(s -> !Objects.equals(s, currentSession))
                    .forEach(aliveSession -> {
                        try {
                            aliveSession.sendMessage(textMessage);
                        } catch (IOException e) {
                            LOGGER.warn(e.getMessage());
                        }
                    });
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        }
    }
}
