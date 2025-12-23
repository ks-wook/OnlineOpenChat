package com.example.OnlineOpenChat.domain.chat.model.response;

import com.example.OnlineOpenChat.common.exception.ErrorCode;
import com.example.OnlineOpenChat.domain.chat.mongo.document.ChatMessage;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.*;

@Schema(description = "Chatting List")
public record ChatListResponse(
        @Schema(description = "결과")
        ErrorCode result,

        @Schema(description = "채팅방의 최근 채팅 내역")
        List<ChatMessage> messages
) {}
