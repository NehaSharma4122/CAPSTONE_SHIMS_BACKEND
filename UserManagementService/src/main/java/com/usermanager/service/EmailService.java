package com.usermanager.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.usermanager.request.Role;
import com.usermanager.request.CreateUserRequest;
import com.usermanager.request.UserResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendUserCreationEmail(CreateUserRequest req, UserResponse createdUser) {

        String subject = "Your SHIMS Portal Account Credentials";

        String body =
                "Hello " + req.getUsername() + ",\n\n" +
                "Your account has been created successfully.\n\n" +
                "Login Details:\n" +
                "Username: " + req.getUsername() + "\n" +
                "Email: " + req.getEmail() + "\n" +
                "Password: " + req.getPassword() + "\n" +
                "Role: " + req.getRole().name() + "\n\n" +
                "Please change your password after first login.\n\n" +
                "Regards,\n" +
                "Admin Team — SHIMS Portal";

        sendMail(req.getEmail(), subject, body);
    }

    public void sendRoleUpdateEmail(UserResponse user, Role newRole) {

        String subject = "Your SHIMS Portal Role Has Been Updated";

        String body =
                "Hello " + user.getUsername() + ",\n\n" +
                "Your account role has been updated.\n\n" +
                "User ID: " + user.getUserId() + "\n" +
                "Email: " + user.getEmail() + "\n" +
                "New Role: " + newRole.name() + "\n\n" +
                "If you believe this is incorrect, please contact support.\n\n" +
                "Regards,\n" +
                "Admin Team — SHIMS Portal";

        sendMail(user.getEmail(), subject, body);
    }

    private void sendMail(String to, String subject, String body) {

        SimpleMailMessage msg = new SimpleMailMessage();

        msg.setTo(to);
        msg.setSubject(subject);
        msg.setText(body);

        mailSender.send(msg);
    }
}
