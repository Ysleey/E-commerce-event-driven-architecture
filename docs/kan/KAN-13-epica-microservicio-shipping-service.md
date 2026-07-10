# KAN-13 - Epica 3: Microservicio de shipping-service

## Objetivo

Construir el segundo microservicio del dominio para gestionar el ciclo de vida logistico de los envios, consumiendo eventos de pedidos y publicando sus propios eventos de estado.

## Alcance implementado

1. Se crea el entorno inicial aislado para `shipping-service`.
2. Se implementa el consumidor Kafka para procesar `OrderCreated`.
3. Se modela la logica de negocio y la persistencia inicial del envio.
4. Se habilita la publicacion de eventos de shipping y la validacion de integracion local.

## Subtareas visibles

- [KAN-14 - Inicializacion y aislamiento de entorno de shipping-service](KAN-14-inicializacion-aislamiento-entorno-shipping-service.md)
- [KAN-15 - Implementacion del consumidor Kafka (@KafkaListener)](KAN-15-implementacion-consumidor-kafka-kafkalistener.md)
- [KAN-16 - Consumo de OrderCreated y generacion de envio](KAN-16-consumo-order-created-generacion-envio.md)
- [KAN-26 - Contrato de consumo OrderCreated + idempotencia](KAN-26-contrato-consumo-idempotencia.md)
- [KAN-27 - Persistencia de envios y estado inicial](KAN-27-persistencia-envios-estado-inicial.md)
- [KAN-28 - Publicacion de eventos de shipping](KAN-28-publicacion-eventos-envio.md)
- [KAN-29 - Pruebas de integracion con Kafka local](KAN-29-pruebas-integracion-kafka-local.md)

## Evidencia tecnica

La captura de Jira muestra la epica 3 con las subtareas de inicio del microservicio shipping-service y las tareas ya cerradas de contrato, persistencia, publicacion e integracion local.

## Definition of Done

- `shipping-service` arranca de forma aislada y prepara su puerto local.
- El flujo `OrderCreated -> Shipping` queda consumido, persistido y publicado.
- La integracion local con Kafka queda verificada.
- La epica queda lista para seguir con mejoras de trazabilidad y resiliencia.