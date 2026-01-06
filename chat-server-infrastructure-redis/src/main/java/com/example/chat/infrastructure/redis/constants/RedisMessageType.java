package com.example.chat.infrastructure.redis.constants;

/**
 * Redis pub/sub 메시지 타입
 */
public enum RedisMessageType {
    /**
     * 유저 초대 알림
     */
    INVITE,

    /**
     * 채팅방 메시지
     */
    NEW_MESSAGE
}
