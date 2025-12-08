package com.example.OnlineOpenChat.domain.user.controller;

import com.example.OnlineOpenChat.domain.user.model.response.UserSearchResponse;
import com.example.OnlineOpenChat.domain.user.service.UserServiceV1;
import com.example.OnlineOpenChat.security.auth.JWTProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User API", description = "V1 User API")
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserControllerV1 {

    private final UserServiceV1 userServiceV1;


    @Operation(
            summary = "User Name List Search",
            description = "User Name을 기반으로 Like 검색 실행"
    )
    @GetMapping("/search/{name}")
    public UserSearchResponse searchUser(
            @PathVariable("name") String name,
            @RequestHeader("Authorization") String authString
    ) {
        String token = JWTProvider.extractToken(authString);
        String user = JWTProvider.getUserId(token);

        return userServiceV1.searchUser(name, user);
    }
}
