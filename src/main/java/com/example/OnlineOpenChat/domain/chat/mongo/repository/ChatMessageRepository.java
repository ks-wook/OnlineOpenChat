package com.example.OnlineOpenChat.domain.chat.mongo.repository;

import com.example.OnlineOpenChat.domain.chat.mongo.document.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatMessageRepository
        extends MongoRepository<ChatMessage, String> {

    List<ChatMessage> findTop100ByRoomIdOrderBySentAtAsc(Long roomId);
}