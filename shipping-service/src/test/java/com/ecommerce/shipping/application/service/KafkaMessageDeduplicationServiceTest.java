package com.ecommerce.shipping.application.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.dao.DataIntegrityViolationException;

import com.ecommerce.shipping.ports.out.ProcessedKafkaEventRepositoryPort;

class KafkaMessageDeduplicationServiceTest {

    @Test
    void shouldReturnTrueWhenFirstProcessing() {
        ProcessedKafkaEventRepositoryPort repository = mock(ProcessedKafkaEventRepositoryPort.class);
        when(repository.existsByEventIdAndConsumerName("evt-1", "shipping-consumer")).thenReturn(false);

        KafkaMessageDeduplicationService service = new KafkaMessageDeduplicationService(repository);

        boolean result = service.registerIfFirstProcessing("evt-1", "shipping-consumer");

        assertTrue(result);
        verify(repository).save("evt-1", "shipping-consumer");
    }

    @Test
    void shouldReturnFalseWhenAlreadyProcessed() {
        ProcessedKafkaEventRepositoryPort repository = mock(ProcessedKafkaEventRepositoryPort.class);
        when(repository.existsByEventIdAndConsumerName("evt-2", "shipping-consumer")).thenReturn(true);

        KafkaMessageDeduplicationService service = new KafkaMessageDeduplicationService(repository);

        boolean result = service.registerIfFirstProcessing("evt-2", "shipping-consumer");

        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenConcurrentInsertOccurs() {
        ProcessedKafkaEventRepositoryPort repository = mock(ProcessedKafkaEventRepositoryPort.class);
        when(repository.existsByEventIdAndConsumerName("evt-3", "shipping-consumer")).thenReturn(false);
        org.mockito.Mockito.doThrow(new DataIntegrityViolationException("duplicate key"))
                .when(repository)
                .save("evt-3", "shipping-consumer");

        KafkaMessageDeduplicationService service = new KafkaMessageDeduplicationService(repository);

        boolean result = service.registerIfFirstProcessing("evt-3", "shipping-consumer");

        assertFalse(result);
    }
}
