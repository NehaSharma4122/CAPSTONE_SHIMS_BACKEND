package com.paymentgateway.controller;

import com.paymentgateway.request.PolicyCartRequest;
import com.paymentgateway.request.RazorPayResponse;
import com.paymentgateway.service.CartService;
import com.paymentgateway.util.RazorpaySignatureUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // STEP-1: Create Razorpay Order
    @PostMapping("/cart/create")
    public RazorPayResponse createCart(@RequestBody PolicyCartRequest request) throws Exception {
        return cartService.createOrder(request);
    }

    // STEP-2: Frontend calls this AFTER success
    @PostMapping("/confirm")
    public ResponseEntity<String> confirmPayment(
            @RequestParam String razorpay_order_id,
            @RequestParam String razorpay_payment_id,
            @RequestParam String razorpay_signature,
            @RequestParam Long policyId) {

        // use key from application.properties
        String secret = cartService.getSecretKey();

        String payload = razorpay_order_id + "|" + razorpay_payment_id;

        boolean isValid = RazorpaySignatureUtil.verify(
                payload,
                razorpay_signature,
                secret
        );

        if (!isValid)
            return ResponseEntity.status(403).body("❌ INVALID PAYMENT SIGNATURE");

        // call PlanPolicyService + activate policy
        cartService.confirmPayment(
                razorpay_order_id,
                razorpay_payment_id,
                policyId
        );

        return ResponseEntity.ok("✔ PAYMENT VERIFIED — POLICY ACTIVATED");
    }
}
