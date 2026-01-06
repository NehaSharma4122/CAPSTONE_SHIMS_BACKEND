package com.claimmanager.service;

import com.claimmanager.request.EmailDetails;

public interface EmailService {
    void sendMail(EmailDetails details);
}