package com.paymentgateway.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.paymentgateway.entity.CartItem;

public interface CartRepository extends JpaRepository<CartItem, Long> {

	Optional<CartItem> findByRazorpayOrderId(String orderId);

    Optional<CartItem> findByPolicyId(Long policyId);}