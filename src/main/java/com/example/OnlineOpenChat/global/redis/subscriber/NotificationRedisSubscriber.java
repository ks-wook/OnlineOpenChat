package com.example.OnlineOpenChat.global.redis.subscriber;

import com.example.OnlineOpenChat.global.redis.RedisMessage;
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
            RedisMessage redisMessage = objectMapper.readValue(messageBody, RedisMessage.class);

            for (Long userId : redisMessage.getTargetUserIds()) {
                messagingTemplate.convertAndSend(
                        "/sub/notification/" + userId,
                        redisMessage
                );
            }

            if (redisMessage.getTargetUserIds() == null) {
                return;
            }

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}
