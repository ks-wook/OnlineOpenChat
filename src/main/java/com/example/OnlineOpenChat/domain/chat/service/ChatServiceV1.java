package com.example.OnlineOpenChat.domain.chat.service;


import java.sql.Timestamp;

import com.example.OnlineOpenChat.domain.chat.model.Message;
import com.example.OnlineOpenChat.domain.chat.model.response.ChatListResponse;
import com.example.OnlineOpenChat.domain.repository.ChatRepository;
import com.example.OnlineOpenChat.domain.repository.entity.Chat;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ChatServiceV1 {

    private final ChatRepository chatRepository;

    public ChatListResponse chatList(String from, String to) {

        // TODO : DB가 아니라 Redis등 채팅 로그를 일시적으로 담아두는 곳에서 채팅 내역을 받아온다.

        /*
        List<Chat> chats = chatRepository.findTop10BySenderOrReceiverOrderByTIDDesc(from, to);

        // Entity -> DTO
        List<Message> res = chats.stream()
                .map(chat -> new Message(chat.getReceiver(), chat.getSender(), chat.getMessage()))
                .collect(Collectors.toList());
         */

        Message mock = Message.builder().to("test2").from("test1").message("test message").build();
        List<Message> messageList = new ArrayList<>();
        messageList.add(mock);

        return new ChatListResponse(messageList);
    }

    @Transactional(transactionManager = "createChatTransacationMansger")
    public void saveChatMessage(Message msg) {
        Chat chat = Chat.builder().
                sender(msg.getFrom()).
                receiver(msg.getTo()).
                message(msg.getMessage()).
                created_at(new Timestamp(System.currentTimeMillis())).
                build();

        chatRepository.save(chat);
    }
}

