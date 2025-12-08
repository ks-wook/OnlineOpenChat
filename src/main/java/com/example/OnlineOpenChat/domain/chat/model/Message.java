package com.example.OnlineOpenChat.domain.chat.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {
    private String to;
    private String from;
    private String message;
}
