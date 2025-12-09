package com.example.OnlineOpenChat.global.redis;

import com.example.OnlineOpenChat.domain.chat.model.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

/**
 * Redis(메시지브로커)로 부터 받은 메시지를 받아서 클라이언트 전달 처리
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ChatRedisSubscriber implements MessageListener {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final SimpMessageSendingOperations messagingTemplate;

    /**
     * Message 수신시 동작
     * @param message
     * @param pattern
     */
    @Override
    public void onMessage(org.springframework.data.redis.connection.Message message, byte[] pattern) {
        try {
            String json = new String(message.getBody());
            Message chatMessage = objectMapper.readValue(json, Message.class);

            // Redis에서 받은 메시지를 WebSocket 구독자에게 전송
            messagingTemplate.convertAndSend("/sub/chat", chatMessage);

        } catch (Exception e) {
            log.error("Redis 메시지 수신 중 오류", e);
        }
    }
}
