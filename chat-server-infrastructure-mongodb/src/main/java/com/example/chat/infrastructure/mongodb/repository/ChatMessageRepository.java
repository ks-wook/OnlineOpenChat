package com.example.chat.infrastructure.mongodb.repository;

import com.example.chat.infrastructure.mongodb.document.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatMessageRepository
        extends MongoRepository<ChatMessage, String> {

    List<ChatMessage> findTop100ByRoomIdOrderBySentAtAsc(Long roomId);
}