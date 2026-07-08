package com.ecommerce.order.adapter.in.messaging;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.SerializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.converter.ConversionException;
import org.springframework.util.backoff.ExponentialBackOff;

@Configuration
public class KafkaResilienceConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaResilienceConfig.class);

    @Bean
    public DeadLetterPublishingRecoverer deadLetterPublishingRecoverer(
            KafkaOperations<Object, Object> kafkaOperations,
            @Value("${app.kafka.topics.dlq-suffix:.dlq}") String dlqSuffix) {
        return new DeadLetterPublishingRecoverer(kafkaOperations,
                (ConsumerRecord<?, ?> record, Exception ex) -> {
                    String dlqTopic = record.topic() + dlqSuffix;
                    LOGGER.error("Routing record to DLQ topic={} originalTopic={} partition={} offset={} error={}",
                            dlqTopic,
                            record.topic(),
                            record.partition(),
                            record.offset(),
                            ex.getMessage());
                    return new TopicPartition(dlqTopic, record.partition());
                });
    }

    @Bean
    public CommonErrorHandler kafkaCommonErrorHandler(
            DeadLetterPublishingRecoverer deadLetterPublishingRecoverer,
            @Value("${app.kafka.retry.max-attempts:3}") int maxAttempts,
            @Value("${app.kafka.retry.initial-interval-ms:1000}") long initialInterval,
            @Value("${app.kafka.retry.multiplier:2.0}") double multiplier,
            @Value("${app.kafka.retry.max-interval-ms:10000}") long maxInterval) {

        ExponentialBackOff backOff = new ExponentialBackOff(initialInterval, multiplier);
        backOff.setInitialInterval(initialInterval);
        backOff.setMaxInterval(maxInterval);
        backOff.setMaxElapsedTime(initialInterval * Math.max(1L, maxAttempts));

        DefaultErrorHandler errorHandler = new DefaultErrorHandler(deadLetterPublishingRecoverer, backOff);
        errorHandler.addNotRetryableExceptions(SerializationException.class, ConversionException.class,
                IllegalArgumentException.class);
        return errorHandler;
    }
}
