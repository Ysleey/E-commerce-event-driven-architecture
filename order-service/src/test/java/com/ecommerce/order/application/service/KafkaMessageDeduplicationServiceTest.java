package com.ecommerce.order.application.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import com.ecommerce.order.ports.out.ProcessedKafkaEventRepositoryPort;

@ExtendWith(MockitoExtension.class)
class KafkaMessageDeduplicationServiceTest {

    @Mock
    private ProcessedKafkaEventRepositoryPort processedKafkaEventRepositoryPort;

    @InjectMocks
    private KafkaMessageDeduplicationService service;

    @Test
    void shouldRegisterWhenEventWasNotProcessedBefore() {
        when(processedKafkaEventRepositoryPort.existsByEventIdAndConsumerName("evt-1", "shipping-service"))
                .thenReturn(false);

        boolean firstProcessing = service.registerIfFirstProcessing("evt-1", "shipping-service");

        assertTrue(firstProcessing);
        verify(processedKafkaEventRepositoryPort).save("evt-1", "shipping-service");
    }

    @Test
    void shouldSkipWhenEventWasAlreadyProcessed() {
        when(processedKafkaEventRepositoryPort.existsByEventIdAndConsumerName("evt-1", "shipping-service"))
                .thenReturn(true);

        boolean firstProcessing = service.registerIfFirstProcessing("evt-1", "shipping-service");

        assertFalse(firstProcessing);
    }

    @Test
    void shouldSkipWhenConcurrentInsertTriggersUniqueConstraint() {
        when(processedKafkaEventRepositoryPort.existsByEventIdAndConsumerName("evt-2", "shipping-service"))
                .thenReturn(false);
        doThrow(new DataIntegrityViolationException("duplicate"))
                .when(processedKafkaEventRepositoryPort)
                .save("evt-2", "shipping-service");

        boolean firstProcessing = service.registerIfFirstProcessing("evt-2", "shipping-service");

        assertFalse(firstProcessing);
    }
}
