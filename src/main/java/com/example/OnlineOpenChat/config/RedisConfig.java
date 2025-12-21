package com.example.OnlineOpenChat.config;

import com.example.OnlineOpenChat.global.redis.subscriber.ChatRedisSubscriber;
import com.example.OnlineOpenChat.global.redis.subscriber.NotificationRedisSubscriber;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

@Configuration
public class RedisConfig {

    /**
     * Pub/Sub + 기본 Redis operations 용 Template
     * (ObjectSerializer 기반) -> JSON 형태 Key Value
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        Jackson2JsonRedisSerializer<Object> serializer =
                new Jackson2JsonRedisSerializer<>(Object.class);

        template.setDefaultSerializer(serializer);
        template.afterPropertiesSet();
        return template;
    }

    /**
     * Redis Stream + Recent Cache 용 Template
     * (String 기반 직렬화)
     */
    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory factory) {
        return new StringRedisTemplate(factory);
    }

    /**
     * Pub/Sub Listener Container
     */
    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            RedisConnectionFactory factory,
            ChatRedisSubscriber chatSubscriber,
            NotificationRedisSubscriber notificationSubscriber,
            @Qualifier("chatMessageTopic") ChannelTopic chatMessageTopic,
            @Qualifier("notificationTopic") ChannelTopic chatNotificationTopic
    ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(factory);

        // 각 토픽에 맞는 리스너(구독자) 등록
        container.addMessageListener(chatSubscriber, chatMessageTopic);
        container.addMessageListener(notificationSubscriber, chatNotificationTopic);

        return container;
    }

    @Bean("chatMessageTopic")
    public ChannelTopic chatMessageTopic() {
        return new ChannelTopic("chat-message");
    }

    @Bean("notificationTopic")
    public ChannelTopic notificationTopic() {
        return new ChannelTopic("chat-notification");
    }
}