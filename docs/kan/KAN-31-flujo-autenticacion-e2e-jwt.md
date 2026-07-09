# KAN-31 - Flujo autenticacion end-to-end con backend JWT

## Objetivo

Validar y cerrar el flujo completo de autenticacion cliente-servidor con JWT, cubriendo login, proteccion de rutas, expiracion de sesion y logout.

## Alcance implementado

1. Login real contra backend `POST /api/v1/auth/login`.
2. Persistencia de sesion con expiracion (`expiresAt`) en cliente.
3. Guard de rutas con redireccion a login y `returnUrl`.
4. Interceptor de errores de autenticacion (`401`) con limpieza de sesion y redireccion.
5. UX de mensajes por contexto de autenticacion (`required`, `expired`, `unauthorized`, `logout`).
6. Logout con limpieza completa de estado y storage.

## Evidencia tecnica

Archivos principales:

- `frontend/src/app/core/auth/services/auth-session.service.ts`
- `frontend/src/app/core/auth/guards/auth.guard.ts`
- `frontend/src/app/core/http/interceptors/auth-error.interceptor.ts`
- `frontend/src/app/features/auth/pages/login-page.component.ts`
- `frontend/src/app/features/auth/pages/login-page.component.html`
- `frontend/src/app/core/layout/app-shell.component.ts`

Pruebas unitarias:

- `frontend/src/app/core/auth/services/auth-session.service.spec.ts`

## Validacion ejecutada

- `frontend` `npm run test -- --watch=false --browsers=ChromeHeadless`: verde (5 tests, 0 failures).
- `frontend` `npm run build`: verde.

## Definition of Done

- Login obtiene token valido desde backend.
- Rutas protegidas bloquean acceso sin sesion.
- Expiracion/invalidez de token se refleja en UX y redireccion.
- Logout limpia sesion y estado de cliente correctamente.
