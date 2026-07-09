# KAN-19 - Servicios de Autenticacion, Gestion de Estado y HttpInterceptor

## Objetivo

Implementar autenticacion JWT en frontend Angular con almacenamiento de sesion, inyeccion automatica del token y proteccion de rutas.

## Alcance implementado

1. Servicio de sesion de autenticacion con `signal` y persistencia en `localStorage`.
2. Servicio `AuthService` conectado al backend real `POST /api/v1/auth/login`.
3. Interceptor HTTP para agregar `Authorization: Bearer <token>`.
4. Guard de rutas para acceso a paginas protegidas.
5. Pantalla de login funcional con flujo de autenticacion y redireccion.

## Evidencia tecnica

Archivos principales:

- `frontend/src/app/core/auth/services/auth-session.service.ts`
- `frontend/src/app/core/auth/services/auth.service.ts`
- `frontend/src/app/core/http/interceptors/auth.interceptor.ts`
- `frontend/src/app/core/auth/guards/auth.guard.ts`
- `frontend/src/app/features/auth/pages/login-page.component.ts`
- `frontend/src/app/features/auth/pages/login-page.component.html`

## Validacion ejecutada

- `frontend` `npm run build`: verde.
- `frontend` `npm run test -- --watch=false --browsers=ChromeHeadless`: verde (2 tests, 0 failures).

## Definition of Done

- Login obtiene token desde backend.
- Token queda persistido en sesion cliente.
- Peticiones protegidas viajan con cabecera bearer automaticamente.
- Rutas funcionales de negocio requieren sesion valida.
