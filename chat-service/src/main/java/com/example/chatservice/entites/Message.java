package com.example.chatservice.entites;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Document(collection = "messages")
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    @Id
    private String id;
    private String message;
    private String sender;
    private String receiver;
    private String channel;
    private Instant date = Instant.now();
}
