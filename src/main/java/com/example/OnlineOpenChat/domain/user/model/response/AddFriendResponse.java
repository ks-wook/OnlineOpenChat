package com.example.OnlineOpenChat.domain.user.model.response;

import com.example.OnlineOpenChat.common.exception.ErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;

public record AddFriendResponse (
        @Schema(description = "결과")
        ErrorCode result
){ }
