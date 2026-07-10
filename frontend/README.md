# Frontend - E-commerce Event Driven Architecture

Frontend Angular 17 construido con componentes standalone y enfoque de calidad tecnica para integracion segura con `order-service`.

## Estructura del modulo

```text
src/app/
|- app.config.ts                  # providers globales (router + http interceptors)
|- app.routes.ts                  # rutas lazy-loaded por feature
|- core/
|  |- auth/                       # login, sesion, guard y modelos de usuario
|  |- http/                       # api client, mapeo de errores e interceptores
|  |- layout/                     # shell principal de navegacion
|- features/
|  |- auth/                       # pantalla de login
|  |- catalog/                    # listado y filtros de productos
|  |- cart/                       # carrito y control de cantidades
|  |- checkout/                   # formulario y creacion de orden
|  |- tracking/                   # seguimiento asincrono de pedido
|- shared/
|  |- ui/                         # componentes reutilizables de presentacion
|  |  |- empty-state-card/
|  |  |- feedback-alert/
|  |  |- page-placeholder/
|  |  |- price-breakdown/
|  |- utils/                      # utilidades transversales
```

## Calidad tecnica (KAN-39)

- Tipado fuerte con interfaces de negocio (`Order`, `Product`, `UserProfile`).
- Estandar de estilo con ESLint + Prettier.
- Modularizacion de UI con componentes shared reutilizables.
- Lazy loading en rutas principales para reducir el bundle inicial.

## Scripts principales

- `npm start`: servidor de desarrollo.
- `npm run start:fast`: desarrollo ligero (sin sourcemaps ni live reload para menor consumo).
- `npm run build`: compilacion de produccion.
- `npm run build:prod`: alias explicito para compilacion de produccion.
- `npm run test -- --watch=false --browsers=ChromeHeadless --progress=false`: pruebas unitarias.
- `npm run lint`: analisis estatico ESLint.
- `npm run lint:fix`: correcciones automaticas de lint.
- `npm run format`: formateo Prettier sobre `src`.

## Integracion local con backend

- Frontend: `http://localhost:4200`
- API base: `http://localhost:8080` (definido en `src/app/core/config/api.config.ts`)
- Requiere `order-service` activo para login, checkout y tracking.

En Windows con restriccion PowerShell para npm scripts:

```powershell
cmd /c npm install
cmd /c npm run build
cmd /c npm run test -- --watch=false --browsers=ChromeHeadless --progress=false
cmd /c npm run lint
```
