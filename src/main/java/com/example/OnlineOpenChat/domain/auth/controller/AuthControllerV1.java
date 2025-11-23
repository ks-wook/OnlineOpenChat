package com.example.OnlineOpenChat.domain.auth.controller;


import com.example.OnlineOpenChat.domain.auth.model.request.CreateUserRequest;
import com.example.OnlineOpenChat.domain.auth.model.response.CreateUserResponse;
import com.example.OnlineOpenChat.domain.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth API", description = "V1 Auth API")
@RestController
@RequestMapping("/api/vi/auth")
@RequiredArgsConstructor
public class AuthControllerV1 {

    private final AuthService authService;

    @Operation(
            summary = "새로운 유저를 생성합니다.",
            description = "새로운 유저 생성"
    )
    @PostMapping("/create-user")
    public CreateUserResponse createUser(CreateUserRequest request) {
        return authService.createUser(request);
    }

}
