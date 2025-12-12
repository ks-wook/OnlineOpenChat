package com.example.OnlineOpenChat.domain.user.model.response;

import com.example.OnlineOpenChat.common.exception.ErrorCode;
import com.example.OnlineOpenChat.domain.user.model.Friend;
import com.example.OnlineOpenChat.domain.user.model.FriendDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record GetFriendListResponse (
        @Schema(description = "결과")
        ErrorCode result,

        @Schema(description = "친구 목록")
        List<FriendDto> friendList
) { }
