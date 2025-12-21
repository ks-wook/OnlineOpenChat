package com.example.OnlineOpenChat.global.redis.subscriber;

// import com.example.OnlineOpenChat.domain.chat.model.Message;
import com.example.OnlineOpenChat.global.redis.RedisMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
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
     * 채팅 메시지 수신 처리 (Deprecated)
     * @param message
     * @param pattern
     */
    /*
    @Override
    public void onMessage(org.springframework.data.redis.connection.Message message, byte[] pattern) {
        try {
            String json = new String(message.getBody());
            Message chatMessage = objectMapper.readValue(json, Message.class);

            // Redis에서 받은 메시지를 WebSocket 구독자에게 전송

            // TODO : 메시지에 포함된   roomId 기반으로 특정 방 구독자에게만 전송하도록 수정 필요
            messagingTemplate.convertAndSend("/sub/chat/", chatMessage);

        } catch (Exception e) {
            log.error("Redis 메시지 수신 중 오류", e);
        }
    }
     */

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String msg = new String(message.getBody());
        log.info("Redis Subscriber가 메시지 수신: {}", msg);

        try {
            RedisMessage chat = objectMapper.readValue(msg, RedisMessage.class);
            
            // RoomId 기반으로 메시지 전송
            messagingTemplate.convertAndSend(
                    "/sub/chat/" + chat.getRoomId(),
                    chat
            );

        } catch (Exception e) {
            log.error("Redis 메시지 발행 중 오류", e);
        }


    }

}
