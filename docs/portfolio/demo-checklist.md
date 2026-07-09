# Checklist de evidencia visual (GIF/Video)

## Objetivo

Registrar una demostracion corta (max 60 segundos) que muestre el flujo tecnico-funcional de la epica para evaluacion de portafolio.

## Flujo sugerido (KAN-41)

1. Login exitoso en frontend.
2. Alta de pedido desde checkout.
3. Seguimiento de pedido por numero/tracking.
4. Confirmacion visual de `correlationId` en logs de `order-service`.
5. Confirmacion de evento publicado en Kafka (si consola disponible).

## Guion de locucion (opcional)

- "Inicio sesion con JWT y el frontend inyecta el token por interceptor."
- "Creo una orden y el backend responde con correlationId para trazabilidad."
- "Consulto seguimiento asincrono y el estado se refresca automaticamente."
- "En backend los logs JSON y el evento Kafka conservan el mismo correlationId."

## Entregables de evidencia

- GIF o video en `docs/portfolio/assets/`.
- Nota breve en Jira con enlace al archivo o al repositorio.

## Herramientas recomendadas

- Windows: Xbox Game Bar / ShareX / OBS.
- Conversor opcional: ffmpeg para exportar GIF comprimido.
