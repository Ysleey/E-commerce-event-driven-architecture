package com.ecommerce.shipping.adapter.out.messaging;

import org.apache.kafka.common.TopicPartition;
import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaListenerConfig {

	@Bean
	DefaultErrorHandler kafkaDefaultErrorHandler(KafkaTemplate<String, Object> kafkaTemplate) {
		DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(
				kafkaTemplate,
				(record, exception) -> new TopicPartition(record.topic() + ".dlq", record.partition()));
		return new DefaultErrorHandler(recoverer, new FixedBackOff(0L, 0L));
	}

	@Bean(name = "kafkaListenerContainerFactory")
	ConcurrentKafkaListenerContainerFactory<Object, Object> kafkaListenerContainerFactory(
			ConcurrentKafkaListenerContainerFactoryConfigurer configurer,
			ConsumerFactory<Object, Object> consumerFactory,
			DefaultErrorHandler kafkaDefaultErrorHandler) {
		ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
		configurer.configure(factory, consumerFactory);
		factory.setCommonErrorHandler(kafkaDefaultErrorHandler);
		return factory;
	}
}