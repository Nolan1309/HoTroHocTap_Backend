package com.example.hotrohoctapbackend.socket;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;


@Component
public class WebSocketEventListener {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    private final OnlineUserTracker userTracker;

    public WebSocketEventListener(OnlineUserTracker userTracker) {
        this.userTracker = userTracker;
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());

        String username = accessor.getUser().getName(); // nếu có bảo mật
        userTracker.setOnline(username);

        logger.info("User Connected: " + username);
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());

        String username = accessor.getUser() != null ? accessor.getUser().getName() : null;
        if (username != null) {
            userTracker.setOffline(username);
            logger.info("User Disconnected: " + username);
        }
    }
}

