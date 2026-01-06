package com.claimmanager.request;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailDetails {
    private String to;
    private String subject;
    private String body;
}