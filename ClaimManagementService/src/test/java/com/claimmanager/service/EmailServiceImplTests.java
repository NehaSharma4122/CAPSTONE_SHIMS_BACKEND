package com.claimmanager.service;

import com.claimmanager.request.EmailDetails;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class EmailServiceImplTests {

    @Test
    void testSendMail() {

        JavaMailSender sender = mock(JavaMailSender.class);

        EmailServiceImpl service = new EmailServiceImpl(sender);

        EmailDetails email = EmailDetails.builder()
                .to("user@mail.com")
                .subject("Test")
                .body("Hello")
                .build();

        service.sendMail(email);

        ArgumentCaptor<SimpleMailMessage> captor =
                ArgumentCaptor.forClass(SimpleMailMessage.class);

        verify(sender).send(captor.capture());

        SimpleMailMessage msg = captor.getValue();

        assertThat(msg.getTo()[0]).isEqualTo("user@mail.com");
        assertThat(msg.getSubject()).isEqualTo("Test");
        assertThat(msg.getText()).isEqualTo("Hello");
    }
}
