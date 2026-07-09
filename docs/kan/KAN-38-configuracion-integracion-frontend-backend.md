# KAN-38 - Configuracion de integracion frontend-backend

## Objetivo
Asegurar una comunicacion fluida y segura entre el frontend y `order-service`, garantizando autenticacion JWT, control de CORS, manejo de errores de autorizacion e integracion de formularios del frontend con endpoints REST.

## Cambios implementados

### 1) Configuracion CORS en backend
Archivo actualizado:
- `order-service/src/main/java/com/ecommerce/order/adapter/in/security/SecurityConfig.java`

Ajustes:
- Se habilito CORS dentro de Spring Security (`http.cors(...)`).
- Se habilitaron preflight requests `OPTIONS /**`.
- Se agrego bean `CorsConfigurationSource` con:
  - origenes permitidos: `http://localhost:4200` y `http://localhost:3000`
  - metodos: `GET, POST, PUT, PATCH, DELETE, OPTIONS`
  - headers permitidos: `Authorization, Content-Type, X-Correlation-Id`
  - header expuesto: `X-Correlation-Id`
  - credenciales habilitadas y cache de preflight.

### 2) Interceptores y manejo global de errores
Archivo actualizado:
- `frontend/src/app/core/http/interceptors/auth-error.interceptor.ts`

Ajustes:
- Se extiende el manejo global para interceptar `401` y `403`.
- Si existe sesion autenticada, se limpia sesion y se redirige a login con `reason` y `returnUrl`.
- Se conserva inyeccion automatica de JWT en `Authorization: Bearer <token>` mediante el interceptor existente.

### 3) Integracion de flujos clave con OrderRestController
Ya operativo en frontend:
- Checkout crea orden via `POST /api/orders`.
- Tracking consulta via `GET /api/orders/by-order-number/{orderNumber}` y `GET /api/orders/by-tracking-number/{trackingNumber}`.

## Criterios de aceptacion (DoD) y cobertura

1. Conexion exitosa: cubierto. El frontend consume endpoints de orden con autenticacion.
2. Persistencia de sesion: cubierto. JWT almacenado y enviado por interceptor.
3. Manejo de errores: cubierto. Redireccion por 401/403 con limpieza de sesion.
4. Validacion visual: cubierto. Checkout y tracking muestran datos API y estados de error.
5. Cero bloqueos CORS: cubierto por configuracion explicita de CORS y preflight.

## Validacion tecnica ejecutada

### Frontend tests
Comando:
- `cmd /c npm run test -- --watch=false --browsers=ChromeHeadless --progress=false`

Resultado:
- `TOTAL: 14 SUCCESS`

### Frontend build
Comando:
- `cmd /c npm run build`

Resultado:
- `Application bundle generation complete.`

### Backend compile/test
Comando ejecutado para validar modulo de ordenes:
- `order-service\\mvnw.cmd clean test`

Resultado:
- `BUILD SUCCESS`
- `Tests run: 16, Failures: 0, Errors: 0, Skipped: 0`
