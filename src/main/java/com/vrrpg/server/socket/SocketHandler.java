package com.vrrpg.server.socket;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import com.vrrpg.core.socket.GameMessage;
import com.vrrpg.core.socket.GameMessageType;
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

        GameMessage.Builder builder = GameMessage.newBuilder();
        JsonFormat.parser().ignoringUnknownFields().merge(message.getPayload(), builder);

        GameMessage gameMessage = builder.build();
        if (gameMessage.getEventType() == GameMessageType.JOIN) {
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

            broadcast(GameMessage.newBuilder()
                            .setEventType(GameMessageType.LEAVE)
                            .setEventSource(eventSource)
                            .putAllEventContent(new HashMap<>())
                            .build(),
                    session);
        }
        aliveSessions.remove(session);
    }

    private void broadcast(GameMessage message, WebSocketSession currentSession) {
        try {
            TextMessage textMessage = new TextMessage(JsonFormat.printer().print(message.toBuilder()));
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
}
