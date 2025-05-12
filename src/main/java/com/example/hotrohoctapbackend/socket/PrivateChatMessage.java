package com.example.hotrohoctapbackend.socket;

import lombok.Data;

import java.time.Instant;

@Data
public class PrivateChatMessage {
    private String from;
    private String to;
    private String content;
    private Instant timestamp;
}
