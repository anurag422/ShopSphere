package com.shopsphere.auth;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AuthServiceApplicationTests {

    @Value("${Google_Client_ID}")
    private String GoogleId;

	@Test
	void contextLoads() {
	}

    @Test
    public void test(){
        System.out.println(GoogleId);
    }

}
