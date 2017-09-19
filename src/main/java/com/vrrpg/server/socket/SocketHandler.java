package com.vrrpg.server.socket;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.util.JsonFormat;
import com.vrrpg.core.communication.model.SocketGameMessage;
import com.vrrpg.core.communication.model.SocketGameMessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
class SocketHandler extends TextWebSocketHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(SocketHandler.class);

    private final Map<WebSocketSession, String> aliveSessions;

    SocketHandler() {
        aliveSessions = new HashMap<>();
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        LOGGER.trace("handleTextMessage - {}, {}", session, message);

        SocketGameMessage gameMessage = parseMessage(message.getPayload(), SocketGameMessage.newBuilder());

        if (gameMessage.getEventType() == SocketGameMessageType.JOIN) {
            aliveSessions.put(session, gameMessage.getEventSource());
        }
        broadcast(gameMessage, session);
        super.handleTextMessage(session, message);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        LOGGER.trace("afterConnectionEstablished - {}", session);
        aliveSessions.put(session, "");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        LOGGER.trace("afterConnectionClosed - {}, {}", session, status);
        if (aliveSessions.containsKey(session)) {
            String eventSource = aliveSessions.get(session);

            broadcast(SocketGameMessage.newBuilder()
                            .setEventType(SocketGameMessageType.LEAVE)
                            .setEventSource(eventSource),
                    session);
        }
        aliveSessions.remove(session);
    }

    private void broadcast(MessageOrBuilder message, WebSocketSession currentSession) {
        try {
            TextMessage textMessage = new TextMessage(JsonFormat.printer().print(message));
            aliveSessions.keySet().stream().filter(s -> !Objects.equals(s, currentSession)).forEach(aliveSession -> {
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
