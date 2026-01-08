package com.example.chat.redis;

import com.example.chat.dto.WebSocketTextMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

@Slf4j
@Service
public class RedisMessageBroker implements MessageListener {

    private static final Logger logger =
            LoggerFactory.getLogger(RedisMessageBroker.class);

    private final RedisTemplate<String, String> redisTemplate;
    private final RedisMessageListenerContainer messageListenerContainer;
    private final ObjectMapper objectMapper;

    /**
     * 서버 고유 ID값
     */
    @Getter
    private final String serverId =
            Optional.ofNullable(System.getenv("HOSTNAME"))
                    .orElse("server-" + System.currentTimeMillis());

    /**
     * 구독중인 방 목록
     */
    private final Set<Long> subscribeRooms =
            ConcurrentHashMap.newKeySet();

    /**
     * local handler (roomId, ChatMessage)
     */
    @Setter
    private volatile BiConsumer<Long, WebSocketTextMessage> localMessageHandler;

    public RedisMessageBroker(
            RedisTemplate<String, String> redisTemplate,
            RedisMessageListenerContainer messageListenerContainer,
            ObjectMapper objectMapper
    ) {
        this.redisTemplate = redisTemplate;
        this.messageListenerContainer = messageListenerContainer;
        this.objectMapper = objectMapper;
    }

    @PreDestroy
    public void cleanup() {

        /**
         * 구독했던 방들 구독 해지
         */
        for (Long roomId : subscribeRooms) {
            unsubscribeFromRoom(roomId);
        }
        logger.info("Removing RedisMessageListenerContainer");
    }

    /**
     * 특정 방 구독
     */
    public void subscribeToRoom(Long roomId) {
        if (subscribeRooms.add(roomId)) {
            ChannelTopic topic = new ChannelTopic("chat.room." + roomId);
            messageListenerContainer.addMessageListener(this, topic);
            logger.info("Subscribed to {}", roomId);
        } else {
            logger.error("Room {} does not exist", roomId);
        }
    }

    /**
     * 특정 방 구독 해제
     */
    public void unsubscribeFromRoom(Long roomId) {
        // TODO : 별도 쓰레드 할당하여 일정 시간마다 유저수가 0명인 방에 대해 구독해지처리

        if (subscribeRooms.remove(roomId)) {
            ChannelTopic topic = new ChannelTopic("chat.room." + roomId);
            messageListenerContainer.removeMessageListener(this, topic);
            logger.info("Unsubscribed from {}", roomId);
        } else {
            logger.error("Room {} does not exist", roomId);
        }
    }

    /**
     * Redis를 통해 다른 인스턴스로 메시지 전파
     */
    public void broadcastToRoom(Long roomId, WebSocketTextMessage payload) {

        // 특정 방 채널토픽으로 publish
        try {
            /*
            DistributedMessage distributedMessage =
                    new DistributedMessage(
                            serverId + "-" + System.currentTimeMillis() + "-" + System.nanoTime(),
                            serverId,
                            roomId,
                            LocalDateTime.now(),
                            payload
                    );

             */

            String json = objectMapper.writeValueAsString(payload);
            redisTemplate.convertAndSend("chat.room." + roomId, json);

            logger.info("Broadcast to {} : {}", roomId, json);

        } catch (Exception e) {
            logger.error("Error broadcast to {}", roomId, e);
        }
    }

    /**
     * subscribed 채널로부터 메시지 수신 처리
     * @param message message must not be {@literal null}.
     * @param pattern pattern matching the channel (if specified) - can be {@literal null}.
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String json = new String(message.getBody(), StandardCharsets.UTF_8);
            //DistributedMessage distributedMessage =
                    //objectMapper.readValue(json, DistributedMessage.class);

            WebSocketTextMessage webSocketTextMessage = objectMapper.readValue(json, WebSocketTextMessage.class);
            BiConsumer<Long, WebSocketTextMessage> handler = localMessageHandler;
            if (handler != null) {
                handler.accept(
                        webSocketTextMessage.getRoomId(),
                        webSocketTextMessage
                );
            }
            else {
                log.error("메시지 핸들러가 설정되지 않았습니다.");
            }

        } catch (Exception e) {
            logger.error("Error in on message", e);
        }
    }

    /**
     * redis pub/sub 분산 메시지 포맷
     */
    /*
    public static class DistributedMessage {

        private String id;
        private String serverId;
        private Long roomId;
        private LocalDateTime timestamp;
        private ChatMessage payload;

        public DistributedMessage() {
        }

        public DistributedMessage(
                String id,
                String serverId,
                Long roomId,
                LocalDateTime timestamp,
                ChatMessage payload
        ) {
            this.id = id;
            this.serverId = serverId;
            this.roomId = roomId;
            this.timestamp = timestamp;
            this.payload = payload;
        }

        public String getId() {
            return id;
        }

        public String getServerId() {
            return serverId;
        }

        public Long getRoomId() {
            return roomId;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public ChatMessage getPayload() {
            return payload;
        }
    }

     */
}
