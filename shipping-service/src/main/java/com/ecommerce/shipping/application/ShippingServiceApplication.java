package com.ecommerce.shipping.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.ecommerce.shipping")
@ConfigurationPropertiesScan
@EntityScan(basePackages = "com.ecommerce.shipping.adapter.out.persistence")
@EnableJpaRepositories(basePackages = "com.ecommerce.shipping.adapter.out.persistence")
public class ShippingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShippingServiceApplication.class, args);
	}
}
