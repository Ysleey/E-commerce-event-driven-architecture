package com.ecommerce.order.adapter.out.persistence;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.ecommerce.order.ports.out.ProcessedKafkaEventRepositoryPort;

@Component
public class ProcessedKafkaEventPersistenceAdapter implements ProcessedKafkaEventRepositoryPort {

    private final ProcessedKafkaEventJpaRepository repository;

    public ProcessedKafkaEventPersistenceAdapter(ProcessedKafkaEventJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean existsByEventIdAndConsumerName(String eventId, String consumerName) {
        return repository.existsByEventIdAndConsumerName(eventId, consumerName);
    }

    @Override
    public void save(String eventId, String consumerName) {
        ProcessedKafkaEventEntity entity = new ProcessedKafkaEventEntity();
        entity.setEventId(eventId);
        entity.setConsumerName(consumerName);
        entity.setProcessedAt(LocalDateTime.now());
        repository.save(entity);
    }
}
