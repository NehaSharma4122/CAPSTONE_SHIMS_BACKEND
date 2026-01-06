package com.claimmanager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.claimmanager.request.EmailDetails;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    
    @Override
    public void sendMail(EmailDetails details) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(details.getTo());
        message.setSubject(details.getSubject());
        message.setText(details.getBody());

        mailSender.send(message);

        System.out.println("EMAIL SENT TO: " + details.getTo());
    }
}
