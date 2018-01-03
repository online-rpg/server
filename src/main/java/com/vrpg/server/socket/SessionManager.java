package com.vrpg.server.socket;

import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.Map;

@Component
@Getter
class SessionManager {

    private final Map<WebSocketSession, String> aliveSessions;

    public SessionManager() {
        this.aliveSessions = new HashMap<>();
    }
}
