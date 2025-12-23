package com.example.OnlineOpenChat.domain.chat.mongo.service;

import com.example.OnlineOpenChat.domain.chat.mongo.document.ChatMessage;
import com.example.OnlineOpenChat.domain.chat.mongo.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    /**
     * 채팅 메시지 저장
     * @param roomId
     * @param userId
     * @param message
     */
    public void saveMessage(Long roomId, Long userId, String senderName, String message) {
        ChatMessage chatMessage = ChatMessage.builder()
                .roomId(roomId)
                .userId(userId)
                .senderName(senderName)
                .message(message)
                .sentAt(System.currentTimeMillis())
                .build();

        chatMessageRepository.save(chatMessage);
    }

    public void saveMessage(ChatMessage msg) {
        ChatMessage chatMessage = ChatMessage.builder()
                .roomId(msg.getRoomId())
                .userId(msg.getUserId())
                .senderName(msg.getSenderName())
                .message(msg.getMessage())
                .sentAt(System.currentTimeMillis())
                .build();

        chatMessageRepository.save(chatMessage);
    }

    /**
     * 최근 100개의 채팅 메시지 조회
     * @param roomId
     * @return
     */
    public List<ChatMessage> getRecentMessages(Long roomId) {
        return chatMessageRepository
                .findTop100ByRoomIdOrderBySentAtAsc(roomId);
    }
}