package com.usermanager.service;

import com.usermanager.request.CreateUserRequest;
import com.usermanager.request.Role;
import com.usermanager.request.UserResponse;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class EmailServiceTest {

    private final JavaMailSender sender = mock(JavaMailSender.class);
    private final EmailService service = new EmailService(sender);

    @Test
    void sendUserCreationEmail_sendsExpectedMail() {

        var req = new CreateUserRequest();
        req.setUsername("Alice");
        req.setEmail("alice@test.com");
        req.setPassword("pass123");
        req.setRole(Role.ROLE_AGENT);

        var created = new UserResponse();
        created.setUserId(10L);
        created.setEmail("alice@test.com");
        created.setUsername("Alice");

        service.sendUserCreationEmail(req, created);

        ArgumentCaptor<SimpleMailMessage> captor =
                ArgumentCaptor.forClass(SimpleMailMessage.class);

        verify(sender).send(captor.capture());

        var msg = captor.getValue();

        assertThat(msg.getTo()[0]).isEqualTo("alice@test.com");
        assertThat(msg.getSubject()).contains("Account Credentials");
        assertThat(msg.getText()).contains("Alice");
        assertThat(msg.getText()).contains("ROLE_AGENT");
    }

    @Test
    void sendRoleUpdateEmail_sendsExpectedMail() {

        var user = new UserResponse();
        user.setUserId(22L);
        user.setUsername("Bob");
        user.setEmail("bob@test.com");

        service.sendRoleUpdateEmail(user, Role.ROLE_HOSPITAL);

        ArgumentCaptor<SimpleMailMessage> captor =
                ArgumentCaptor.forClass(SimpleMailMessage.class);

        verify(sender).send(captor.capture());

        var msg = captor.getValue();

        assertThat(msg.getTo()[0]).isEqualTo("bob@test.com");
        assertThat(msg.getSubject()).contains("Role Has Been Updated");
        assertThat(msg.getText()).contains("ROLE_HOSPITAL");
    }
}
