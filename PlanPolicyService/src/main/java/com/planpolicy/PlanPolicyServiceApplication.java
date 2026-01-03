package com.planpolicy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class PlanPolicyServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlanPolicyServiceApplication.class, args);
	}

}
