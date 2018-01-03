package com.vrpg.server.socket.handler;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.vrpg.communication.model.networking.socketmessages.SocketMessageType;
import com.vrpg.communication.model.networking.socketmessages.messages.GetStateMessage;
import com.vrpg.server.socket.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
class GetStateMessageHandler extends SocketMessageHandler<GetStateMessage> {
    private static final Logger LOGGER = LoggerFactory.getLogger(GetStateMessageHandler.class);

    GetStateMessageHandler(SessionManager sessionManager) {
        super(sessionManager);
    }

    @Override
    GetStateMessage parse(ByteString bytes) throws InvalidProtocolBufferException {
        LOGGER.trace("parse - {}", bytes);
        return GetStateMessage.parseFrom(bytes);
    }

    @Override
    public SocketMessageType getMessageType() {
        return SocketMessageType.GET_STATE_SOCKET;
    }
}
