package lk.ijse.agrosmart_systembackend.util;

import lk.ijse.agrosmart_systembackend.repository.PasswordResetTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class TokenCleanupScheduler {

    private final PasswordResetTokenRepository tokenRepository;

    @Scheduled(cron = "0 0 */1 * * *") // Run every hour
    @Transactional
    public void cleanupExpiredTokens() {
        tokenRepository.deleteExpiredOrUsedTokens(LocalDateTime.now());
    }
}