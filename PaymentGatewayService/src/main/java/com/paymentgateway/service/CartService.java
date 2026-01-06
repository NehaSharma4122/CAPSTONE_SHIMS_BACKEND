package com.paymentgateway.service;

import com.paymentgateway.client.PolicyFeignClient;
import com.paymentgateway.entity.CartItem;
import com.paymentgateway.entity.PaymentStatus;
import com.paymentgateway.repository.CartRepository;
import com.paymentgateway.request.PolicyCartRequest;
import com.paymentgateway.request.PolicyResponse;
import com.paymentgateway.request.RazorPayResponse;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {

    private final PolicyFeignClient policyClient;
    private final CartRepository cartRepo;

    private final String CURRENCY = "INR";

    @Value("${razorpay.key.id}")
    private String razorpayKeyId;

    @Value("${razorpay.key.secret}")
    private String razorpaySecret;

    private RazorpayClient razorpay;

    public String getSecretKey() {
        return razorpaySecret;
    }

    public String getPublicKey() {
        return razorpayKeyId;
    }

    @PostConstruct
    public void init() throws Exception {
        razorpay = new RazorpayClient(razorpayKeyId, razorpaySecret);
    }

    public RazorPayResponse createOrder(PolicyCartRequest request) throws Exception {

        PolicyResponse policy = policyClient.getPolicy(request.policyId());
        
        var existing = cartRepo.findByPolicyId(policy.id());

        CartItem cart = existing.orElseGet(CartItem::new);
        cart.setPolicyId(policy.id());
        cart.setUserId(policy.userId());
        cart.setAmount(policy.premiumPaid());
        cart.setStatus(PaymentStatus.PENDING_PAYMENT);
        cart.setRazorpayPaymentId(null); 
        cartRepo.save(cart);

        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", Math.round(policy.premiumPaid() * 100)); // paise
        orderRequest.put("currency", CURRENCY);
        orderRequest.put("receipt", "POLICY_" + policy.id()+System.currentTimeMillis());

        Order order = razorpay.orders.create(orderRequest);

        cart.setRazorpayOrderId(order.get("id"));
        cartRepo.save(cart);

        return new RazorPayResponse(
                order.get("id"),
                Math.round(policy.premiumPaid()*100),
                order.get("currency")
        );
    }

    public String confirmPayment(String orderId, String paymentId, Long policyId) {

        CartItem cart = cartRepo.findByRazorpayOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (cart.getStatus() == PaymentStatus.PAID) {
            return "Payment already processed";
        }

        cart.setRazorpayPaymentId(paymentId);
        cart.setStatus(PaymentStatus.PAID);
        cartRepo.save(cart);

        // call PlanPolicyService to activate policy
        policyClient.confirmPayment(policyId);

        return "Payment confirmed & policy activated";
    }
}
