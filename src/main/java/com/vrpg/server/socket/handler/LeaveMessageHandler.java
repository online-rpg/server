package com.vrpg.server.socket.handler;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.vrpg.communication.model.networking.socketmessages.SocketMessageType;
import com.vrpg.communication.model.networking.socketmessages.messages.LeaveMessage;
import com.vrpg.server.socket.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

@Component
class LeaveMessageHandler extends SocketMessageHandler<LeaveMessage> {
    private static final Logger LOGGER = LoggerFactory.getLogger(LeaveMessageHandler.class);
    private SessionManager sessionManager;

    LeaveMessageHandler(SessionManager sessionManager) {
        super(sessionManager);
        this.sessionManager = sessionManager;
    }

    @Override
    void handleMessage(WebSocketSession session, LeaveMessage message) {
        LOGGER.trace("handleMessage - {}, {}", session, message);

        if (sessionManager.getAliveSessions().containsKey(session)) {
            sessionManager.getAliveSessions().remove(session);
        }
    }

    @Override
    LeaveMessage parse(ByteString bytes) throws InvalidProtocolBufferException {
        LOGGER.trace("parse - {}", bytes);
        return LeaveMessage.parseFrom(bytes);
    }

    @Override
    public SocketMessageType getMessageType() {
        LOGGER.trace("getMessageType");
        return SocketMessageType.LEAVE;
    }
}
