package com.ecommerce.shipping.integration;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import com.ecommerce.shipping.adapter.in.messaging.event.OrderEventMessage;
import com.ecommerce.shipping.adapter.in.messaging.event.OrderEventPayload;
import com.ecommerce.shipping.adapter.out.messaging.event.ShippingEventMessage;
import com.ecommerce.shipping.adapter.out.persistence.ShippingJpaRepository;
import com.ecommerce.shipping.adapter.out.persistence.ShippingPersistenceAdapter;
import com.ecommerce.shipping.application.ShippingServiceApplication;
import com.ecommerce.shipping.domain.model.Shipping;
import com.ecommerce.shipping.domain.model.ShippingStatus;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(classes = ShippingServiceApplication.class)
@ActiveProfiles("test")
@EmbeddedKafka(partitions = 1, topics = {
		"ecommerce.order.events.it",
		"ecommerce.shipping.events.it",
		"ecommerce.order.events.it.dlq"
})
@TestPropertySource(properties = {
		"spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}",
		"spring.kafka.listener.auto-startup=true",
		"spring.kafka.consumer.group-id=shipping-service-it",
		"app.kafka.topics.order-events=ecommerce.order.events.it",
		"app.kafka.topics.shipping-events=ecommerce.shipping.events.it"
})
class ShippingKafkaIntegrationTest {

	@Autowired
	private ShippingJpaRepository shippingJpaRepository;

	@Autowired
	private ShippingPersistenceAdapter shippingPersistenceAdapter;

	@Autowired
	private ObjectMapper objectMapper;

	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapServers;

	@Value("${app.kafka.topics.order-events}")
	private String orderEventsTopic;

	@Value("${app.kafka.topics.shipping-events}")
	private String shippingEventsTopic;

	private KafkaProducer<String, String> producer;

	@BeforeEach
	void setUp() {
		shippingJpaRepository.deleteAll();
		producer = new KafkaProducer<>(producerProperties());
	}

	@AfterEach
	void tearDown() {
		if (producer != null) {
			producer.close(Duration.ofSeconds(2));
		}
	}

	@Test
	void shouldCreateShippingAndPublishShippingCreatedEvent() throws Exception {
		long orderId = Math.abs(UUID.randomUUID().getMostSignificantBits());
		String eventId = UUID.randomUUID().toString();
		String correlationId = "corr-9001";
		KafkaConsumer<String, String> shippingEventsConsumer = createConsumer(shippingEventsTopic, "shipping-it-created");

		try {
			produceOrderCreatedEvent(eventId, orderId, correlationId, "ORD-9001");

			Shipping shipping = awaitShipping(orderId);
			assertNotNull(shipping);
			assertEquals(orderId, shipping.getOrderId());
			assertEquals(ShippingStatus.PENDING, shipping.getStatus());

			ConsumerRecord<String, String> shippingCreatedRecord = awaitRecordMatching(
					shippingEventsConsumer,
					record -> record.value().contains("\"eventType\":\"ShippingCreated\"")
							&& record.value().contains("\"orderId\":" + orderId)
							&& record.value().contains("\"correlationId\":\"" + correlationId + "\""),
					15_000L);
			assertEquals(String.valueOf(orderId), shippingCreatedRecord.key());

			ShippingEventMessage shippingCreated = objectMapper.readValue(shippingCreatedRecord.value(), ShippingEventMessage.class);
			assertEquals("ShippingCreated", shippingCreated.getEventType());
			assertEquals(orderId, shippingCreated.getPayload().getOrderId());
			assertEquals("PENDING", shippingCreated.getPayload().getStatus());
			assertEquals(correlationId, shippingCreated.getCorrelationId());
		} finally {
			shippingEventsConsumer.close(Duration.ofSeconds(2));
		}
	}

	@Test
	void shouldIgnoreDuplicateOrderCreatedEvent() throws Exception {
		long orderId = Math.abs(UUID.randomUUID().getMostSignificantBits());
		String eventId = UUID.randomUUID().toString();
		KafkaConsumer<String, String> shippingEventsConsumer = createConsumer(shippingEventsTopic, "shipping-it-duplicate");

		try {
			String payload = buildOrderCreatedJson(eventId, orderId, "corr-9002", "ORD-9002");
			producer.send(new ProducerRecord<>(orderEventsTopic, String.valueOf(orderId), payload)).get(5, TimeUnit.SECONDS);
			producer.send(new ProducerRecord<>(orderEventsTopic, String.valueOf(orderId), payload)).get(5, TimeUnit.SECONDS);

			Shipping shipping = awaitShipping(orderId);
			assertNotNull(shipping);
			assertEquals(1L, shippingJpaRepository.count());

			List<ConsumerRecord<String, String>> records = collectRecords(shippingEventsConsumer, 5_000L);
			long matchingShippingCreated = records.stream()
					.filter(record -> record.value().contains("\"eventType\":\"ShippingCreated\"")
							&& record.value().contains("\"orderId\":" + orderId))
					.count();
			assertEquals(1L, matchingShippingCreated);
		} finally {
			shippingEventsConsumer.close(Duration.ofSeconds(2));
		}
	}

	@Test
	void shouldSendMalformedMessageToDlq() throws Exception {
		KafkaConsumer<String, String> dlqConsumer = createConsumer(orderEventsTopic + ".dlq", "shipping-it-dlq");

		try {
			shippingJpaRepository.deleteAll();
			String malformedJson = "{not-valid-json-" + UUID.randomUUID();
			producer.send(new ProducerRecord<>(orderEventsTopic, "broken-key", malformedJson)).get(5, TimeUnit.SECONDS);

			ConsumerRecord<String, String> dlqRecord = awaitRecordMatching(dlqConsumer, record -> true, 15_000L);
			assertNotNull(dlqRecord);
			assertEquals("broken-key", dlqRecord.key());
			assertEquals(orderEventsTopic + ".dlq", dlqRecord.topic());
			assertEquals(0L, shippingJpaRepository.count());
		} finally {
			dlqConsumer.close(Duration.ofSeconds(2));
		}
	}

	private void produceOrderCreatedEvent(String eventId, long orderId, String correlationId, String orderNumber) throws Exception {
		String payload = buildOrderCreatedJson(eventId, orderId, correlationId, orderNumber);
		producer.send(new ProducerRecord<>(orderEventsTopic, String.valueOf(orderId), payload)).get(5, TimeUnit.SECONDS);
	}

	private String buildOrderCreatedJson(String eventId, long orderId, String correlationId, String orderNumber) throws Exception {
		OrderEventPayload payload = new OrderEventPayload();
		payload.setOrderId(orderId);
		payload.setOrderNumber(orderNumber);
		payload.setCustomerId(1001L);
		payload.setProductId(555L);
		payload.setTotalAmount(new java.math.BigDecimal("135.0"));
		payload.setCurrency("EUR");
		payload.setStatus("CREATED");

		OrderEventMessage message = new OrderEventMessage();
		message.setEventId(eventId);
		message.setEventType("OrderCreated");
		message.setEventVersion("1.0");
		message.setProducer("order-service");
		message.setTraceId(UUID.randomUUID().toString());
		message.setCorrelationId(correlationId);
		message.setPartitionKey(String.valueOf(orderId));
		message.setPayload(payload);

		return objectMapper.writeValueAsString(message);
	}

	private Shipping awaitShipping(long orderId) throws InterruptedException {
		long deadline = System.currentTimeMillis() + 15_000L;
		while (System.currentTimeMillis() < deadline) {
			Optional<Shipping> shipping = shippingPersistenceAdapter.findByOrderId(orderId);
			if (shipping.isPresent()) {
				return shipping.get();
			}
			LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(200L));
		}
		return null;
	}

	private ConsumerRecord<String, String> awaitRecordMatching(KafkaConsumer<String, String> consumer, java.util.function.Predicate<ConsumerRecord<String, String>> predicate, long timeoutMs) {
		long deadline = System.currentTimeMillis() + timeoutMs;
		while (System.currentTimeMillis() < deadline) {
			ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(250L));
			for (ConsumerRecord<String, String> record : records) {
				if (predicate.test(record)) {
					return record;
				}
			}
		}
		return null;
	}

	private List<ConsumerRecord<String, String>> collectRecords(KafkaConsumer<String, String> consumer, long timeoutMs) {
		List<ConsumerRecord<String, String>> records = new ArrayList<>();
		long deadline = System.currentTimeMillis() + timeoutMs;
		while (System.currentTimeMillis() < deadline) {
			ConsumerRecords<String, String> polled = consumer.poll(Duration.ofMillis(250L));
			for (ConsumerRecord<String, String> record : polled) {
				records.add(record);
			}
		}
		return records;
	}

	private KafkaConsumer<String, String> createConsumer(String topic, String groupId) {
		KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerProperties(groupId));
		consumer.subscribe(Collections.singletonList(topic));
		long deadline = System.currentTimeMillis() + 10_000L;
		while (consumer.assignment().isEmpty() && System.currentTimeMillis() < deadline) {
			consumer.poll(Duration.ofMillis(250L));
		}
		consumer.assignment().forEach(partition -> consumer.seek(partition, consumer.endOffsets(Collections.singleton(partition)).get(partition)));
		return consumer;
	}

	private Properties producerProperties() {
		Properties properties = new Properties();
		properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		properties.put(ProducerConfig.ACKS_CONFIG, "all");
		return properties;
	}

	private Properties consumerProperties(String groupId) {
		Properties properties = new Properties();
		properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
		return properties;
	}
}