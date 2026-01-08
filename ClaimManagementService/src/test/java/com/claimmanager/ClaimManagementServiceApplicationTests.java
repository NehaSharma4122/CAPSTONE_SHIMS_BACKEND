package com.claimmanager;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.claimmanager.jwt.JWTUtils;

@SpringBootTest
class ClaimManagementServiceApplicationTests {
	
	@MockBean
    private JWTUtils jwtUtils;
	
	@Test
	void contextLoads() {
        ClaimManagementServiceApplication.main(new String[]{});
	}

}
