package com.example.chatservice.dtos;

import lombok.Data;

@Data
public class MessageRequest {
    private String message;
    private String sender;
    private String receiver;
    private String channel;
}