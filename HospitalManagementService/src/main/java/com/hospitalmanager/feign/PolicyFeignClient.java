package com.hospitalmanager.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "PlanPolicyService")
public interface PolicyFeignClient {

    @GetMapping("/api/plans/exists/{planId}")
    boolean doesPlanExist(@PathVariable Long planId);
}
