# KAN-40 - CI minima frontend (observabilidad y trazabilidad backend)

## Objetivo
Implementar observabilidad y trazabilidad minima en `order-service` para diagnostico rapido de flujo REST + eventos Kafka, con foco en `correlationId`, errores estructurados y endpoints operativos.

## Cambios implementados

### 1) Correlation ID end-to-end
Archivos:
- `order-service/src/main/java/com/ecommerce/order/adapter/in/observability/CorrelationIdFilter.java`
- `order-service/src/main/java/com/ecommerce/order/adapter/in/controller/OrderRestController.java`
- `order-service/src/main/java/com/ecommerce/order/application/service/OrderUseCaseService.java`
- `order-service/src/main/java/com/ecommerce/order/adapter/out/messaging/KafkaOrderEventPublisher.java`

Resultado:
- Correlation ID generado/propagado por request.
- Correlation ID presente en logs y en payload de eventos Kafka.
- Header `X-Correlation-Id` retornado al cliente.

### 2) Logging estructurado JSON
Archivo:
- `order-service/src/main/resources/application.properties`

Resultado:
- Consola en formato JSON legible por herramientas de agregacion.
- Campos clave: timestamp, level, service, traceId, correlationId, logger, message.

### 3) Manejo global de excepciones con correlationId
Archivos:
- `order-service/src/main/java/com/ecommerce/order/adapter/in/controller/handler/ApiErrorResponse.java`
- `order-service/src/main/java/com/ecommerce/order/adapter/in/controller/handler/GlobalExceptionHandler.java`
- `order-service/src/main/java/com/ecommerce/order/adapter/in/security/SecurityConfig.java`

Resultado:
- Errores de negocio/tecnicos incluyen `correlationId` en JSON de respuesta.
- Respuestas de seguridad 401/403 incluyen `correlationId`.

### 4) Actuators de monitoreo
Archivos:
- `order-service/src/main/resources/application.properties`
- `order-service/src/main/java/com/ecommerce/order/adapter/in/security/SecurityConfig.java`

Resultado:
- Exposicion: `/actuator/health`, `/actuator/info`, `/actuator/metrics`.
- Seguridad:
  - `health` publico
  - `info` con roles `ADMIN` o `LOGISTICS`
  - `metrics` solo `ADMIN`

### 5) Documentacion de trazabilidad
Archivo:
- `README.md`

Resultado:
- Se agrego diagrama de secuencia de propagacion de `correlationId` desde frontend hasta evento Kafka.

## Validacion tecnica ejecutada

### Backend tests
Comando:
- `order-service\\mvnw.cmd test`

Resultado obtenido:
- `BUILD SUCCESS`
- `Tests run: 18, Failures: 0, Errors: 0, Skipped: 0`

## Cobertura de criterios de aceptacion
1. Trazabilidad total: cumplido.
2. Logs JSON: cumplido.
3. Respuesta al cliente con correlationId: cumplido.
4. Endpoint de salud y monitoreo: cumplido.
5. Prueba de concepto correlacion REST->Kafka: cumplido por wiring y logs de publicacion con correlationId.
