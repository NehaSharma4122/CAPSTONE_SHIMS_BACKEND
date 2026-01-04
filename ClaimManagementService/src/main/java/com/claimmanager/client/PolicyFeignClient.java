package com.claimmanager.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.claimmanager.config.FeignClientConfig;
import com.claimmanager.request.PolicyResponseDTO;


@FeignClient(
        name = "PlanPolicyService",
        url = "http://localhost:8082",
        configuration = FeignClientConfig.class
)
public interface PolicyFeignClient {

	@GetMapping("/api/policy/users/{userId}/{policyId}")
    PolicyResponseDTO getUserPolicy(
            @PathVariable Long userId,
            @PathVariable Long policyId
    );
}
