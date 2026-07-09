# KAN-16 - Consumo de OrderCreated y generacion de envio

## Objetivo

Convertir el evento `OrderCreated` de `ecommerce.order.events.v1` en un envio inicial en `shipping-service`.

## Alcance implementado

1. El consumer de Kafka procesa solo `OrderCreated` v1.
2. El `orderId` del evento se mapea al caso de uso de creacion de envio.
3. Se crea un envio en estado `PENDING`.
4. La creacion se ejecuta solo para el primer procesamiento del evento, respetando la idempotencia de KAN-26.

## Regla funcional

1. Si llega un `OrderCreated` valido y no duplicado, se crea el envio.
2. Si el evento ya fue procesado, no se vuelve a crear el envio.
3. Si el contrato no es valido, el mensaje se rechaza.

## Evidencia tecnica

Archivos principales:

- `shipping-service/src/main/java/com/ecommerce/shipping/adapter/in/messaging/OrderEventsConsumer.java`
- `shipping-service/src/main/java/com/ecommerce/shipping/application/service/ShippingUseCaseService.java`
- `shipping-service/src/main/java/com/ecommerce/shipping/domain/model/Shipping.java`
- `shipping-service/src/main/java/com/ecommerce/shipping/ports/in/CreateShippingUseCase.java`

Pruebas:

- `shipping-service/src/test/java/com/ecommerce/shipping/adapter/in/messaging/OrderEventsConsumerTest.java`

## Definition of Done

- `OrderCreated` dispara la creacion de un envio.
- `orderId` del contrato se conserva como identificador funcional.
- El flujo respeta el dedupe persistente.
- El comportamiento queda cubierto por tests.