package com.ecommerce.shipping.adapter.out.messaging;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin.NewTopics;

@Configuration
public class KafkaTopicsConfig {

	@Bean
	NewTopics kafkaTopics(
			@Value("${app.kafka.topics.order-events:ecommerce.order.events.v1}") String orderEventsTopic,
			@Value("${app.kafka.topics.shipping-events:ecommerce.shipping.events.v1}") String shippingEventsTopic) {
		return new NewTopics(
				TopicBuilder.name(orderEventsTopic).partitions(3).replicas(3).build(),
				TopicBuilder.name(orderEventsTopic + ".dlq").partitions(3).replicas(3).build(),
				TopicBuilder.name(shippingEventsTopic).partitions(3).replicas(3).build(),
				TopicBuilder.name(shippingEventsTopic + ".dlq").partitions(3).replicas(3).build());
	}
}