package com.example.chatservice.controllers;

import com.example.chatservice.dtos.MessageRequest;
import com.example.chatservice.dtos.MessageResponse;
import com.example.chatservice.entites.Message;
import com.example.chatservice.repositories.ChatMessageRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "api/chat")
public class ChatMessageController {
    private final ChatMessageRepository chatMessageRepository;
    private final ModelMapper modelMapper;

    public ChatMessageController(ChatMessageRepository chatMessageRepository, ModelMapper modelMapper) {

        this.chatMessageRepository = chatMessageRepository;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void postChat(@Valid @RequestBody MessageRequest messageRequest) {

        Message message = modelMapper.map(messageRequest, Message.class);

        chatMessageRepository.insert(message).subscribe();
    }

    @GetMapping(value = "stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<MessageResponse> streamMessages(@RequestParam String channel) {

        return chatMessageRepository.findByChannel(channel)
                .map(message -> modelMapper.map(message, MessageResponse.class));
    }
}