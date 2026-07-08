package com.ecommerce.order.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessedKafkaEventJpaRepository extends JpaRepository<ProcessedKafkaEventEntity, Long> {

    boolean existsByEventIdAndConsumerName(String eventId, String consumerName);
}
