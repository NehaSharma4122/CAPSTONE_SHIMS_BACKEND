package com.paymentgateway.client;

import com.paymentgateway.config.FeignClientConfig;
import com.paymentgateway.request.PolicyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name="PlanPolicyService", configuration=FeignClientConfig.class)
public interface PolicyFeignClient {

    // Get policy details (fetch premium amount)
    @GetMapping("/api/user/policy/details/{policyId}")
    PolicyResponse getPolicy(@PathVariable Long policyId);

    @PutMapping("/api/policy/payment/confirm/{policyId}")
    void confirmPayment(@PathVariable Long policyId);
}



