package com.hospitalmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class HospitalManagementServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(HospitalManagementServiceApplication.class, args);
	}

}
