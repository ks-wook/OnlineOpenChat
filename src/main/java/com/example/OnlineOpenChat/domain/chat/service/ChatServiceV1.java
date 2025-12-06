package com.example.OnlineOpenChat.domain.chat.service;


import com.example.OnlineOpenChat.domain.chat.model.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ChatServiceV1 {

    /**
     * 채팅 메시지 저장
     * @param msg
     */
    public void saveChatMessage(Message msg) {

        // TODO : 채팅 메시지 저장

        // 한번에 여러 메시지가 들어온경우, DB에 채팅을 바로 저장하려 하면 DB I/O가 너무 자주 일어남


    }
}
