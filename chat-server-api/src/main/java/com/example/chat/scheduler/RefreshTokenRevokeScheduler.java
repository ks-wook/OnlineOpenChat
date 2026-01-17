package com.example.chat.scheduler;

import com.example.chat.repository.AuthRefreshTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenRevokeScheduler {

    private final AuthRefreshTokenRepository authRefreshTokenRepository;

    /**
     * 매일 UTC 기준 자정 실행
     */
    @Transactional
    @Scheduled(cron = "0 0 0 * * *", zone = "UTC")
    public void revokeExpiredRefreshTokens() {

        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);

        int updatedCount = authRefreshTokenRepository.revokeExpiredTokens(now);

        log.info("[RefreshTokenScheduler] revoked expired tokens count={}", updatedCount);
    }
}