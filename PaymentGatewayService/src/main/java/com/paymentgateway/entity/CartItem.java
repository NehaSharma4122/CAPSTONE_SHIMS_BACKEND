package com.paymentgateway.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "policy_cart")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long policyId;
    private Long userId;

    private Double amount;   // from policy.premiumPaid

    private String razorpayOrderId;
    private String razorpayPaymentId;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status = PaymentStatus.PENDING_PAYMENT;
}
