package com.vrrpg.server.socket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    private final ObjectMapper objectMapper;
    private final Map<WebSocketSession, String> aliveSessions;

    SocketHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        aliveSessions = new HashMap<>();
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        LOGGER.trace("handleTextMessage - {}, {}", session, message);
        GameMessage gameMessage = objectMapper.readValue(message.getPayload(), GameMessage.class);
        if ("join".equalsIgnoreCase(gameMessage.getEventName())) {
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
            broadcast(new GameMessage("leave", eventSource, new HashMap<>()), session);
        }
        aliveSessions.remove(session);
    }

    private void broadcast(GameMessage message, WebSocketSession currentSession) {
        try {
            TextMessage textMessage = new TextMessage(objectMapper.writeValueAsString(message));
            aliveSessions.keySet().stream().filter(s -> !Objects.equals(s, currentSession)).forEach(aliveSession -> {
                try {
                    aliveSession.sendMessage(textMessage);
                } catch (IOException e) {
                    LOGGER.warn(e.getMessage());
                }
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
