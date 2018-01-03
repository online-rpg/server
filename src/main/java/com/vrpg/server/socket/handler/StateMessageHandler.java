package com.vrpg.server.socket.handler;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.vrpg.communication.model.networking.socketmessages.SocketMessageType;
import com.vrpg.communication.model.networking.socketmessages.messages.StateMessage;
import com.vrpg.server.socket.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
class StateMessageHandler extends SocketMessageHandler<StateMessage> {
    private static final Logger LOGGER = LoggerFactory.getLogger(StateMessageHandler.class);

    StateMessageHandler(SessionManager sessionManager) {
        super(sessionManager);
    }

    @Override
    StateMessage parse(ByteString bytes) throws InvalidProtocolBufferException {
        LOGGER.trace("parse - {}", bytes);
        return StateMessage.parseFrom(bytes);
    }

    @Override
    public SocketMessageType getMessageType() {
        LOGGER.trace("getMessageType");
        return SocketMessageType.STATE;
    }
}
