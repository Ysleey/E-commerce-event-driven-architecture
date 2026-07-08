package com.ecommerce.order.application.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.ecommerce.order.ports.out.ProcessedKafkaEventRepositoryPort;

@Service
public class KafkaMessageDeduplicationService {

    private final ProcessedKafkaEventRepositoryPort processedKafkaEventRepositoryPort;

    public KafkaMessageDeduplicationService(ProcessedKafkaEventRepositoryPort processedKafkaEventRepositoryPort) {
        this.processedKafkaEventRepositoryPort = processedKafkaEventRepositoryPort;
    }

    public boolean registerIfFirstProcessing(String eventId, String consumerName) {
        if (processedKafkaEventRepositoryPort.existsByEventIdAndConsumerName(eventId, consumerName)) {
            return false;
        }

        try {
            processedKafkaEventRepositoryPort.save(eventId, consumerName);
            return true;
        } catch (DataIntegrityViolationException ex) {
            return false;
        }
    }
}
