package com.claimmanager.request;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EmailDetailsTests {

    @Test
    void testBuilderAndFields() {

        EmailDetails email = EmailDetails.builder()
                .to("user@mail.com")
                .subject("Test Subject")
                .body("Message")
                .build();

        assertThat(email.getTo()).isEqualTo("user@mail.com");
        assertThat(email.getSubject()).isEqualTo("Test Subject");
        assertThat(email.getBody()).isEqualTo("Message");
    }

    @Test
    void testNoArgsConstructor() {

        EmailDetails email = new EmailDetails();

        email.setTo("abc@mail.com");
        email.setSubject("Hello");
        email.setBody("Body text");

        assertThat(email.getTo()).isEqualTo("abc@mail.com");
        assertThat(email.getSubject()).isEqualTo("Hello");
        assertThat(email.getBody()).isEqualTo("Body text");
    }
}
