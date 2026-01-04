package com.claimmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ClaimManagementServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClaimManagementServiceApplication.class, args);
	}

}
