package com.authentication.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.authentication.entity.User;
import com.authentication.repository.AuthRepository;
import com.authentication.request.ChangePasswordRequest;

@Service
@RequiredArgsConstructor
public class PasswordChangeService {

    private final AuthRepository repo;
    private final PasswordEncoder encoder;
    private final PasswordPolicyService policy;

    public User changePassword(User user, ChangePasswordRequest req) {

        if (!encoder.matches(req.getOldPassword(), user.getPassword()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Old password incorrect");

        policy.validateStrength(req.getNewPassword());
        policy.validateHistory(user, req.getNewPassword());

        String hash = encoder.encode(req.getNewPassword());

        policy.updateHistory(user, hash);

        user.setPassword(hash);

        return repo.save(user);
    }
}

