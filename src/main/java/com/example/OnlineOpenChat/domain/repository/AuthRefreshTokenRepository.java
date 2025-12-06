package com.example.OnlineOpenChat.domain.repository;

import com.example.OnlineOpenChat.domain.repository.entity.AuthRefreshToken;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthRefreshTokenRepository extends JpaRepository<AuthRefreshToken, Long> {
    // 가장 최근에 발급된 RefreshToken을 획득
    Optional<AuthRefreshToken> findByUserIdOrderByCreatedAt(Long userId);

    // 발급된 refreshToken 값 조회
    Optional<AuthRefreshToken> findByRefreshToken(String refreshToken);
}
