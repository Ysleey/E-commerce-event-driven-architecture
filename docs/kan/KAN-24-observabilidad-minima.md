# KAN-24 - Observabilidad mínima (logs, correlation-id, health)

## Objetivo
Implementar base de observabilidad en order-service para trazabilidad de flujos HTTP/Kafka y diagnóstico local/CI.

## Entregables esperados
1. Logs estructurados con campos mínimos:
   - timestamp
   - service
   - level
   - traceId
   - correlationId
   - message
2. Propagación de correlation-id en cada request HTTP.
3. Correlation-id en eventos Kafka publicados.
4. Endpoint de health operativo para Docker/local.
5. Guía breve de troubleshooting.

## Criterios de aceptación
1. Logs contienen timestamp/service/level/correlation-trace.
2. Correlation-id se genera o propaga por request.
3. Correlation-id viaja en eventos Kafka.
4. Health endpoint usable en local.
5. Errores técnicos/negocio quedan trazables.

## Propuesta de implementación paso a paso
1. Crear filtro HTTP de correlation-id:
   - Lee cabecera X-Correlation-Id.
   - Si no existe, genera UUID.
   - Lo guarda en MDC para logging.
2. Configurar patrón de logs incluyendo correlation-id desde MDC.
3. Exponer /actuator/health con spring-boot-starter-actuator.
4. Asegurar que el publisher Kafka use correlation-id de contexto/request cuando esté disponible.
5. Añadir prueba de integración que confirme presencia de correlation-id en flujo básico.

## Evidencia requerida para Jira
- Captura/extracto de logs con correlation-id.
- Respuesta de /actuator/health en estado UP.
- Nota en Jira con ejemplo de trazado end-to-end.

## Definition of Done
- Se puede seguir un flujo completo por correlation-id.
- Hay evidencia reproducible de health y logs.
- Configuración validada en entorno local.
