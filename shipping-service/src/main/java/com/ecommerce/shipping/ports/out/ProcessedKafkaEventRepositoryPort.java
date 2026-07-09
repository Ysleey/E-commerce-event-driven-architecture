package com.ecommerce.shipping.ports.out;

public interface ProcessedKafkaEventRepositoryPort {

    boolean existsByEventIdAndConsumerName(String eventId, String consumerName);

    void save(String eventId, String consumerName);
}
