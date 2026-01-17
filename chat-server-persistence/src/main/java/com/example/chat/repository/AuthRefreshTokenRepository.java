package com.example.chat.repository;

import com.example.chat.model.entity.AuthRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface AuthRefreshTokenRepository extends JpaRepository<AuthRefreshToken, Long> {
    // 가장 최근에 발급된 RefreshToken을 획득
    Optional<AuthRefreshToken> findByUserIdOrderByCreatedAt(Long userId);

    // 발급된 refreshToken 값 조회
    Optional<AuthRefreshToken> findByRefreshToken(String refreshToken);

    @Modifying(clearAutomatically = true)
    @Query("""
        update AuthRefreshToken t
        set t.isRevoked = true
        where t.isRevoked = false
          and t.expiredAt < :now
    """)
    int revokeExpiredTokens(@Param("now") LocalDateTime now);
}
