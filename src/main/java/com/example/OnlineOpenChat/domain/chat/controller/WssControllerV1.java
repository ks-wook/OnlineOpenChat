package com.example.OnlineOpenChat.domain.chat.controller;

import com.example.OnlineOpenChat.domain.chat.model.Message;
import com.example.OnlineOpenChat.domain.chat.service.ChatServiceV1;
import com.example.OnlineOpenChat.global.redis.ChatRedisPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
@Slf4j
/**
 * 채팅 메시지 처리 컨트롤러
 * 참고문서 : https://docs.spring.io/spring-framework/reference/web/websocket/stomp/handle-annotations.html
 */
public class WssControllerV1 {

    private final ChatServiceV1 chatServiceV1;
    private final ChatRedisPublisher publisher;


    /**
     * 채팅 메시지 처리
     * @param from
     * @param msg
     * @return
     */
    @MessageMapping("/chat/message/{from}")
    // @SendTo("/sub/chat")
    public void receivedMessage(
            @DestinationVariable String from,
            Message msg)
    {
        log.info("Message Received -> From: {}, To: {}, msg: {}", from, msg.getTo(), msg.getMessage());
        // chatServiceV1.saveChatMessage(msg);
        // return msg;
        
        // TODO : 메시지 Redis에 임시 저장

        // 메시지 브로커에게 메시지 퍼블리싱
        publisher.publish(msg);
    }
}
