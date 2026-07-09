# KAN-30 - Modelo de integracion con APIs y manejo de errores HTTP

## Objetivo

Definir una capa HTTP tipada y reusable en Angular, con convencion de errores consistente y comportamiento controlado ante fallos de red y backend.

## Alcance implementado

1. `ApiClientService` central para operaciones `GET`, `POST` y `PATCH`.
2. `HttpErrorMapperService` para transformar errores HTTP en modelo de dominio UI.
3. Modelo `ApiError` tipado con categorias (`validation`, `auth`, `forbidden`, `not-found`, `network`, `server`, `unknown`).
4. Interceptor de error de autenticacion para limpiar sesion y redirigir a login en `401`.
5. Integracion de interceptores y `HttpClient` en configuracion global de app.

## Evidencia tecnica

Archivos principales:

- `frontend/src/app/core/http/services/api-client.service.ts`
- `frontend/src/app/core/http/services/http-error-mapper.service.ts`
- `frontend/src/app/core/http/models/api-error.model.ts`
- `frontend/src/app/core/http/interceptors/auth-error.interceptor.ts`
- `frontend/src/app/app.config.ts`

## Validacion ejecutada

- `frontend` `npm run build`: verde.
- `frontend` `npm run test -- --watch=false --browsers=ChromeHeadless`: verde (2 tests, 0 failures).

## Definition of Done

- Existe una convencion unica para errores HTTP en frontend.
- Integracion con backend encapsulada en una capa reusable.
- Sesion de usuario se invalida correctamente en respuestas `401`.
- El consumo HTTP queda preparado para KAN-20, KAN-31 y KAN-32.
