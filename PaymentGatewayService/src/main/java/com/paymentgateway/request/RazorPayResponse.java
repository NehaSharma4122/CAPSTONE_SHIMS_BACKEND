package com.paymentgateway.request;

public record RazorPayResponse(
        String orderId,
        double amount,
        String currency
) {}
