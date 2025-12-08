package com.example.OnlineOpenChat.domain.auth.controller;


import com.example.OnlineOpenChat.domain.auth.model.request.CreateUserRequest;
import com.example.OnlineOpenChat.domain.auth.model.request.LoginRequest;
import com.example.OnlineOpenChat.domain.auth.model.response.CreateUserResponse;
import com.example.OnlineOpenChat.domain.auth.model.response.LoginResponse;
import com.example.OnlineOpenChat.domain.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth API", description = "V1 Auth API")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthControllerV1 {

    private final AuthService authService;

    @Operation(
            summary = "새로운 유저를 생성합니다.",
            description = "새로운 유저 생성"
    )
    @PostMapping("/create-user")
    public CreateUserResponse createUser(
            @RequestBody @Valid CreateUserRequest request) {
        return authService.createUser(request);
    }

    @Operation(
            summary = "로그인 처리",
            description = "유저의 로그인을 진행"
    )
    @PostMapping("/login")
    public LoginResponse loginResponse(
            @RequestBody @Valid LoginRequest request, HttpServletResponse httpResponse) {
        return authService.login(request, httpResponse);
    }

    @Operation(
            summary = "토큰 값을 통해 유저명 획득",
            description = "Jwt 토큰값에서 유저명을 추출합니다."
    )
    @GetMapping("/verify-token/{token}")
    public String getUserFromToken(
            @PathVariable String token) {
        return authService.getUserFromToken(token);
    }
}
