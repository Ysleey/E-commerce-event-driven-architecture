package com.ecommerce.order;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.ecommerce.order.application.OrderServiceApplication;

@SpringBootTest(classes = OrderServiceApplication.class)
@ActiveProfiles("test")
class OrderServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
