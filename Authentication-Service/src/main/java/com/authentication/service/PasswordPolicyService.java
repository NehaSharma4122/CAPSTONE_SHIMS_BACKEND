package com.authentication.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.authentication.entity.User;

import java.time.Duration;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PasswordPolicyService {

    private final PasswordEncoder encoder;

    private static final int MIN_LENGTH = 8;
    private static final int PASSWORD_HISTORY_LIMIT = 5;
    private static final int EXPIRY_DAYS = 60;
    private static final int MAX_FAILED_ATTEMPTS = 5;
    private static final int LOCK_MINUTES = 15;

    public boolean isExpired(User user) {
        if (user.getLastPasswordChangedAt() == null) return true;

        long days = Duration
                .between(user.getLastPasswordChangedAt().toInstant(), new Date().toInstant())
                .toDays();

        return days >= EXPIRY_DAYS;
    }

    public boolean isLocked(User user) {
        return user.getAccountLockedUntil() != null
                && user.getAccountLockedUntil().after(new Date());
    }

    public void applyFailedAttempt(User user) {
        user.setFailedAttempts(user.getFailedAttempts() + 1);

        if (user.getFailedAttempts() >= MAX_FAILED_ATTEMPTS) {
            user.setAccountLockedUntil(
                    new Date(System.currentTimeMillis() + LOCK_MINUTES * 60 * 1000)
            );
            user.setFailedAttempts(0);
        }
    }

    public void clearLock(User user) {
        user.setFailedAttempts(0);
        user.setAccountLockedUntil(null);
    }

    public void validateStrength(String pwd) {

        if (pwd.length() < MIN_LENGTH)
            throw new IllegalArgumentException("Password too short");

        if (!pwd.matches(".*[A-Z].*"))
            throw new IllegalArgumentException("Password must contain uppercase");

        if (!pwd.matches(".*[a-z].*"))
            throw new IllegalArgumentException("Password must contain lowercase");

        if (!pwd.matches(".*[0-9].*"))
            throw new IllegalArgumentException("Password must contain a number");

        if (!pwd.matches(".*[!@#$%^&*()\\-_=+{};:,<.>/?].*"))
            throw new IllegalArgumentException("Password must contain a symbol");
    }

    public void validateHistory(User user, String newPwd) {
        if (user.getPasswordHistory() == null) return;

        for (String old : user.getPasswordHistory()) {
            if (encoder.matches(newPwd, old)) {
                throw new IllegalArgumentException(
                        "You cannot reuse previous passwords");
            }
        }
    }

    public void updateHistory(User user, String hash) {

        List<String> history = user.getPasswordHistory();
        if (history == null) history = new ArrayList<>();

        history.add(0, hash);

        if (history.size() > PASSWORD_HISTORY_LIMIT)
            history = history.subList(0, PASSWORD_HISTORY_LIMIT);

        user.setPasswordHistory(history);
        user.setLastPasswordChangedAt(new Date());
        user.setForcePasswordChange(false);
    }
}
