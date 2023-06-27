package com.example.chatservice.dtos;

import lombok.Data;

import java.time.Instant;

@Data
public class MessageResponse {
    private String message;
    private String sender;
    private String receiver;
    private Instant date;
}