package com.example.OnlineOpenChat.domain.auth.service;

import com.example.OnlineOpenChat.domain.auth.model.request.CreateUserRequest;
import com.example.OnlineOpenChat.domain.auth.model.response.CreateUserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    /*
     * 유저 회원 가입 처리
     */
    public CreateUserResponse createUser(CreateUserRequest request) {
        return new CreateUserResponse((request.name()));
    }
}
