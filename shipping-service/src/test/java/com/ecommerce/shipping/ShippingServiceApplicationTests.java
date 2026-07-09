package com.ecommerce.shipping;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.ecommerce.shipping.application.ShippingServiceApplication;

@SpringBootTest(classes = ShippingServiceApplication.class)
@ActiveProfiles("test")
class ShippingServiceApplicationTests {

	@Test
	void contextLoads() {
	}
}
