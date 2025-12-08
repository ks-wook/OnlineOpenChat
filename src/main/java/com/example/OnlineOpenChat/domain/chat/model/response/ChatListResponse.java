package com.example.OnlineOpenChat.domain.chat.model.response;

import com.example.OnlineOpenChat.domain.chat.model.Message;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.*;

@Schema(description = "Chatting List")
public record ChatListResponse(
        @Schema(description = "return Messagae : []")
        List<Message> result
) {}
