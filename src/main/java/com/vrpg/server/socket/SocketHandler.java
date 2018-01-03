package com.vrpg.server.socket;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;
import com.vrpg.communication.model.networking.envelopes.SocketEnvelope;
import com.vrpg.communication.model.networking.socketmessages.SocketMessage;
import com.vrpg.communication.model.networking.socketmessages.SocketMessageType;
import com.vrpg.communication.model.networking.socketmessages.messages.LeaveMessage;
import com.vrpg.server.socket.handler.SocketMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Component
class SocketHandler extends TextWebSocketHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(SocketHandler.class);

    private final SessionManager sessionManager;
    private final Map<SocketMessageType, SocketMessageHandler> socketMessageHandlers;

    SocketHandler(SessionManager sessionManager, Collection<SocketMessageHandler> messageHandlers) {
        this.sessionManager = sessionManager;
        socketMessageHandlers = messageHandlers.stream()
                .collect(Collectors.toMap(SocketMessageHandler::getMessageType, o -> o));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        LOGGER.trace("handleTextMessage - {}, {}", session, message);

        SocketEnvelope envelope = parseMessage(message.getPayload(), SocketEnvelope.newBuilder());
        SocketMessage socketMessage = envelope.getSocketMessage();

        SocketMessageHandler messageHandler = socketMessageHandlers.get(socketMessage.getMessageType());
        if (messageHandler != null) {
            messageHandler.handleMessage(session, envelope);
        } else {
            LOGGER.warn("No messagehandler for type - {}", socketMessage.getMessageType());
        }

        super.handleTextMessage(session, message);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        LOGGER.trace("afterConnectionEstablished - {}", session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        LOGGER.trace("afterConnectionClosed - {}, {}", session, status);
        Map<WebSocketSession, String> aliveSessions = sessionManager.getAliveSessions();

        if (aliveSessions.containsKey(session)) {
            SocketEnvelope socketEnvelope = SocketEnvelope.newBuilder()
                    .setSocketMessage(
                            SocketMessage.newBuilder()
                                    .setMessageType(SocketMessageType.LEAVE)
                                    .setMessage(LeaveMessage.newBuilder()
                                            .setEventSource(aliveSessions.get(session))
                                            .build()
                                            .toByteString())
                                    .build())
                    .build();

            socketMessageHandlers.get(SocketMessageType.LEAVE).handleMessage(session, socketEnvelope);
        }
        aliveSessions.remove(session);
    }

    @SuppressWarnings("unchecked")
    private <T extends Message> T parseMessage(String textMessage, T.Builder builder) {
        try {
            JsonFormat.parser().ignoringUnknownFields().merge(textMessage, builder);
            return (T) builder.build();
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        }
    }
}
