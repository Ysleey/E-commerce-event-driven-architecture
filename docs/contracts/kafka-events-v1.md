# Kafka Event Contract v1

## Purpose

This document defines the event contract used for communication between microservices in the e-commerce platform.
The contract is designed for portfolio-grade architecture: explicit versioning, idempotency, and backwards-compatible evolution.

## Scope

Producer (current):
- order-service

Consumers (current and planned):
- shipping-service
- inventory-service
- payment-service
- notification-service

## Kafka topology

- Cluster target: 3 brokers
- Replication factor (prod target): 3
- Partitions per topic (initial target): 3
- Key strategy: aggregate id (orderId)
- Serialization: JSON with schema-governed envelope

## Topic naming convention

Use domain-oriented names:

- ecommerce.order.events.v1
- ecommerce.shipping.events.v1
- ecommerce.payment.events.v1
- ecommerce.inventory.events.v1
- ecommerce.notification.events.v1

## Event envelope (mandatory)

All events MUST contain this envelope:

```json
{
  "eventId": "6f7fdcc6-f4af-4c48-9a9f-68c7af9ee381",
  "eventType": "OrderCreated",
  "eventVersion": "1.0",
  "occurredAt": "2026-07-08T13:00:00Z",
  "producer": "order-service",
  "traceId": "trace-8f7d6c5b",
  "correlationId": "corr-ORD-2026-0001",
  "partitionKey": "1",
  "payload": {}
}
```

Field rules:

- eventId: globally unique UUID per published event.
- eventType: semantic event name in PascalCase.
- eventVersion: contract version for compatibility management.
- occurredAt: UTC ISO-8601 timestamp.
- producer: service name.
- traceId: technical trace chain identifier.
- correlationId: business flow identifier.
- partitionKey: Kafka key value, generally orderId.
- payload: event-specific business data.

## Order domain events (v1)

### 1) OrderCreated

Topic: ecommerce.order.events.v1

```json
{
  "eventType": "OrderCreated",
  "eventVersion": "1.0",
  "payload": {
    "orderId": 1,
    "orderNumber": "ORD-2026-0001",
    "customerId": 1001,
    "productId": 555,
    "totalAmount": 135.0,
    "currency": "EUR",
    "status": "CREATED"
  }
}
```

Expected consumers:
- inventory-service: reserve stock
- payment-service: initiate payment authorization
- notification-service: send confirmation

### 2) OrderShipped

Topic: ecommerce.order.events.v1

```json
{
  "eventType": "OrderShipped",
  "eventVersion": "1.0",
  "payload": {
    "orderId": 1,
    "orderNumber": "ORD-2026-0001",
    "trackingNumber": "TRK-ES-999000111",
    "shippingMethod": "EXPRESS",
    "status": "SHIPPED"
  }
}
```

Expected consumers:
- notification-service: shipment email/SMS

### 3) OrderCompleted

Topic: ecommerce.order.events.v1

```json
{
  "eventType": "OrderCompleted",
  "eventVersion": "1.0",
  "payload": {
    "orderId": 1,
    "orderNumber": "ORD-2026-0001",
    "status": "COMPLETED"
  }
}
```

Expected consumers:
- analytics/reporting
- notification-service

### 4) OrderCancelled

Topic: ecommerce.order.events.v1

```json
{
  "eventType": "OrderCancelled",
  "eventVersion": "1.0",
  "payload": {
    "orderId": 1,
    "orderNumber": "ORD-2026-0001",
    "reason": "Customer requested cancellation",
    "status": "CANCELLED"
  }
}
```

Expected consumers:
- inventory-service: release reservation
- payment-service: stop authorization or trigger compensation
- notification-service

### 5) OrderReturnRequested

Topic: ecommerce.order.events.v1

```json
{
  "eventType": "OrderReturnRequested",
  "eventVersion": "1.0",
  "payload": {
    "orderId": 1,
    "orderNumber": "ORD-2026-0001",
    "reason": "Product damaged",
    "status": "RETURN_REQUESTED"
  }
}
```

Expected consumers:
- shipping-service: reverse logistics workflow
- payment-service: prepare refund process

### 6) OrderRefundRequested

Topic: ecommerce.order.events.v1

```json
{
  "eventType": "OrderRefundRequested",
  "eventVersion": "1.0",
  "payload": {
    "orderId": 1,
    "orderNumber": "ORD-2026-0001",
    "reason": "Product damaged",
    "status": "REFUND_REQUESTED"
  }
}
```

Expected consumers:
- payment-service: execute refund
- notification-service

## Delivery and reliability rules

- Delivery semantics: at-least-once.
- Consumers MUST be idempotent using eventId + aggregate keys.
- Producer SHOULD use outbox pattern for transactional integrity.
- Retries MUST include exponential backoff policy.
- Poison messages MUST be redirected to DLQ topic.

### Consumer de-duplication mandatory rule

To avoid processing the same business event twice after broker or consumer restarts,
every consumer MUST persist processed event IDs in a durable store with a unique constraint.

Recommended minimal table:

```sql
CREATE TABLE processed_kafka_events (
  event_id VARCHAR(100) PRIMARY KEY,
  consumer_name VARCHAR(100) NOT NULL,
  processed_at TIMESTAMP NOT NULL
);
```

Processing rule:

1. Read event.
2. Check if event_id already exists.
3. If exists: skip business processing and commit offset.
4. If not exists: process business logic, store event_id, then commit offset.

## Suggested DLQ topics

- ecommerce.order.events.v1.dlq
- ecommerce.shipping.events.v1.dlq
- ecommerce.payment.events.v1.dlq

## Contract evolution policy

- Backward compatible changes:
  - add optional fields
  - add new event types
- Breaking changes:
  - remove required fields
  - rename fields
  - incompatible type changes

Rules:

1. Non-breaking changes: keep major version (1.x).
2. Breaking changes: publish new major topic version (v2).
3. Deprecation period: keep both versions active during migration window.

## Security and compliance guidelines

- Avoid personal sensitive data in payload unless strictly needed.
- Use transport encryption for broker connections in non-local environments.
- Restrict topic ACLs by service account.
- Log event metadata for audit (eventId, correlationId, producer, timestamp).

## Observability fields (required in logs)

When publishing or consuming, log at least:

- eventId
- eventType
- correlationId
- traceId
- topic
- partition
- offset
- processingResult

## Initial implementation mapping

Current HTTP actions in order-service should map to events:

- POST /api/orders -> OrderCreated
- PUT /api/orders/{id}/ship -> OrderShipped
- PUT /api/orders/{id}/complete -> OrderCompleted
- PUT /api/orders/{id}/cancel -> OrderCancelled
- PUT /api/orders/{id}/return -> OrderReturnRequested
- PUT /api/orders/{id}/refund -> OrderRefundRequested
