package com.vrrpg.server.socket;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
class GameMessage {
    private final String eventName;
    private final String eventSource;
    private final Map<String, String> eventContent;
}
