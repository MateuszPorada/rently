package com.example.chatservice.repositories;

import com.example.chatservice.entites.Message;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ChatMessageRepository extends ReactiveMongoRepository<Message, String> {

    @Tailable
    Flux<Message> findByChannel(String channel);
}