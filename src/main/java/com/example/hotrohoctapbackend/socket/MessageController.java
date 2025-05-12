package com.example.hotrohoctapbackend.socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // Gửi thông báo đến 1 user cụ thể
    public void notifyUser(String username, NotificationMessage message) {
        messagingTemplate.convertAndSendToUser(username, "/queue/notification", message);
    }

    // Gửi thông báo đến tất cả người dùng
    @MessageMapping("/notify/all")
    public void notifyAll(NotificationMessage message) {
        messagingTemplate.convertAndSend("/topic/notification/all", message);
    }

    // Gửi thông báo theo role
    public void notifyRole(String role, NotificationMessage message) {
        messagingTemplate.convertAndSend("/topic/notification/role/" + role, message);
    }

    // Chat 1-1
    @MessageMapping("/chat.private")
    public void sendPrivateChat(PrivateChatMessage message) {
        messagingTemplate.convertAndSendToUser(message.getTo(), "/queue/chat", message);
    }

    // Chat nhóm
    @MessageMapping("/chat.group.{groupId}")
    public void sendGroupChat(@DestinationVariable String groupId, GroupChatMessage message) {
        messagingTemplate.convertAndSend("/topic/chat/group/" + groupId, message);
    }
}