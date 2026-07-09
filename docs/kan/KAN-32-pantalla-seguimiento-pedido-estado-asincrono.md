# KAN-32 - Pantalla de seguimiento de pedido con estado asincrono

## Objetivo
Implementar una pantalla funcional de seguimiento de pedido que consulte el backend por numero de pedido o numero de tracking, represente el estado actual y permita actualizacion asincrona del progreso.

## Cambios realizados

### Frontend - Tracking feature
- Se reemplazo el placeholder de seguimiento por una pantalla funcional con formulario de busqueda.
- Se agrego soporte de consulta por:
  - numero de pedido
  - numero de tracking
- Se incorporaron estados de UX:
  - carga inicial
  - actualizacion en segundo plano
  - sin resultados (not found)
  - error generico
- Se implemento auto-refresh cada 10 segundos (con toggle) para reflejar cambios asincronos de estado.
- Se agrego timeline visual de progreso para estados principales del flujo logistico.
- Se agregaron paneles de detalle para:
  - datos de pedido
  - direccion de envio
  - direccion de facturacion
  - metodo logistico

### Archivos creados
- `frontend/src/app/features/tracking/models/tracking-order.model.ts`
- `frontend/src/app/features/tracking/services/tracking-order.service.ts`
- `frontend/src/app/features/tracking/pages/tracking-page.component.html`

### Archivos actualizados
- `frontend/src/app/features/tracking/pages/tracking-page.component.ts`

## Alineacion con backend
Estados confirmados en backend (`OrderUseCaseService`):
- `CREATED`
- `SHIPPED`
- `COMPLETED`
- `CANCELLED`
- `RETURN_REQUESTED`
- `REFUND_REQUESTED`

La vista muestra estado actual del pedido y progreso del flujo base `CREATED -> SHIPPED -> COMPLETED`, manteniendo visibilidad del estado real recibido desde API.

## Validacion tecnica

### Build frontend
Comando ejecutado:
- `cmd /c npm run build`

Resultado:
- Build exitoso.

### Tests frontend
Comando ejecutado:
- `cmd /c npm run test -- --watch=false --browsers=ChromeHeadless --progress=false`

Resultado:
- `TOTAL: 5 SUCCESS`

## Criterios de aceptacion cubiertos
- Pantalla de seguimiento implementada y conectada a backend.
- Estado del pedido visible y actualizable asincronamente.
- Manejo de errores HTTP y no encontrados en UI.
- UX consistente con la identidad visual definida en Epic 4.
