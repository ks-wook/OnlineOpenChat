package com.example.chat.redis.publisher;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;


/**
 * 알림 메시지 퍼블리셔 - ex) 채팅방 알림 초대 등...
 */
@Component
public class NotificationRedisPublisher {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic notificationTopic;

    public NotificationRedisPublisher(
            RedisTemplate<String, Object> redisTemplate,
            @Qualifier("notificationTopic") ChannelTopic topic
    ) {
        this.redisTemplate = redisTemplate;
        this.notificationTopic = topic;
    }

    /**
     * 메시지 브로커(Redis)로 알림 메시지 전송
     * @param notification
     */
    public void publishNotification(Object notification) {
        redisTemplate.convertAndSend(
                notificationTopic.getTopic(),
                notification
        );
    }
}
