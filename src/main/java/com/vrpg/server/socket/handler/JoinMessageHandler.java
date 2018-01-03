package com.vrpg.server.socket.handler;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.vrpg.communication.model.networking.socketmessages.SocketMessageType;
import com.vrpg.communication.model.networking.socketmessages.messages.JoinMessage;
import com.vrpg.server.socket.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

@Component
class JoinMessageHandler extends SocketMessageHandler<JoinMessage> {
    private static final Logger LOGGER = LoggerFactory.getLogger(JoinMessageHandler.class);

    private SessionManager sessionManager;

    JoinMessageHandler(SessionManager sessionManager) {
        super(sessionManager);
        this.sessionManager = sessionManager;
    }

    @Override
    public void handleMessage(WebSocketSession session, JoinMessage message) {
        LOGGER.trace("handleMessage - {}", message);
        String clientId = message.getEventSource();

        if (clientId == null || clientId.isEmpty()) {
            LOGGER.warn("Client id is null - {}", clientId);
            return;
        }

        sessionManager.getAliveSessions().put(session, clientId);
    }

    @Override
    public JoinMessage parse(ByteString bytes) throws InvalidProtocolBufferException {
        LOGGER.trace("parse - {}", bytes);
        return JoinMessage.parseFrom(bytes);
    }

    @Override
    public SocketMessageType getMessageType() {
        LOGGER.trace("getMessageType");
        return SocketMessageType.JOIN_SOCKET;
    }
}
