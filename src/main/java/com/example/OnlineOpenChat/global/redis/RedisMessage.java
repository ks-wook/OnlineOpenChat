package com.example.OnlineOpenChat.global.redis;

import com.example.OnlineOpenChat.common.Constants.RedisMessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RedisMessage {
    // TODO : 추후에 enum 으로 변경 고려
    /**
     * 메시지의 타입
     */
    private RedisMessageType type;   // INVITE, NEW_MESSAGE

    /**
     * 어떤 방을 target으로 전송하는지 데이터
     */
    private Long roomId;

    /**
     * 메시지 내용
     */
    private String message;

    /**
     * 메시지를 보낸 유저 이름
     */
    private String senderName;


    /**
     * 메시지를 받을 대상 유저 ID 배열 -> 초대 알림 전송 시에만 데이터가 포함됨
     */
    private List<Long> targetUserIds;
}
