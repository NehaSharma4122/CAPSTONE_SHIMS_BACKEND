package com.paymentgateway.request;
import java.time.LocalDate;

public record PolicyResponse(
        Long id,
        Long userId,
        Double premiumPaid,
        LocalDate startDate,
        String policyStatus
) {}
