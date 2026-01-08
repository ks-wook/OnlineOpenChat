package com.example.chat.redis.subscriber;

import com.example.chat.dto.WebSocketTextMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.data.redis.connection.Message;

import org.springframework.stereotype.Component;


/**
 * 유저 알림 채널 처리 subscriber
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationRedisSubscriber implements MessageListener {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final SimpMessageSendingOperations messagingTemplate;

    /**
     * Message 수신시 동작
     * @param message
     * @param pattern
     */
    @Override
    public void onMessage(
            Message message,
            byte[] pattern
    ) {
        String messageBody = message.toString();
        log.info("NotificationRedisSubscriber - onMessage : {}", messageBody);

        try {
            WebSocketTextMessage webSocketTextMessage = objectMapper.readValue(messageBody, WebSocketTextMessage.class);

            for (Long userId : webSocketTextMessage.getTargetUserIds()) {
                messagingTemplate.convertAndSend(
                        "/sub/notification/" + userId,
                        webSocketTextMessage
                );
            }

            if (webSocketTextMessage.getTargetUserIds() == null) {
                return;
            }

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}
