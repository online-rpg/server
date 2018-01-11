package com.vrpg.server.api.socket.handler;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.vrpg.communication.model.networking.socketmessages.SocketMessageType;
import com.vrpg.communication.model.networking.socketmessages.messages.MoveMessage;
import com.vrpg.server.api.socket.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
class MoveMessageHandler extends SocketMessageHandler<MoveMessage> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MoveMessageHandler.class);

    MoveMessageHandler(SessionManager sessionManager) {
        super(sessionManager);
    }

    @Override
    public MoveMessage parse(ByteString bytes) throws InvalidProtocolBufferException {
        LOGGER.trace("parse - {}", bytes);
        return MoveMessage.parseFrom(bytes);
    }

    @Override
    public SocketMessageType getMessageType() {
        LOGGER.trace("getMessageType");
        return SocketMessageType.MOVE_SOCKET;
    }
}
