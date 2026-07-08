package com.ecommerce.order.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.ecommerce.order")
@ConfigurationPropertiesScan
@EntityScan(basePackages = "com.ecommerce.order.adapter.out.persistence")
@EnableJpaRepositories(basePackages = "com.ecommerce.order.adapter.out.persistence")
public class OrderServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderServiceApplication.class, args);
	}

}
