package com.hospitalmanager.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.hospitalmanager.request.ClaimStatsDTO;

@FeignClient(name = "ClaimManagementService")
public interface ClaimFeignClient {
	
	@GetMapping("/api/claims/hospital/{hospitalId}")
    List<ClaimStatsDTO> getClaimsByHospital(@PathVariable("hospitalId") Long hospitalId);
	
}
