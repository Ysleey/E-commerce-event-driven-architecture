# KAN-18 - Inicializacion del Proyecto Angular y Setup de Tailwind CSS

## Objetivo

Crear el frontend base en Angular 17 con una arquitectura escalable, routing inicial y una base visual profesional con Tailwind CSS.

## Alcance implementado

1. Scaffold completo de Angular 17 en carpeta raiz `frontend`.
2. Integracion de Tailwind CSS con tema base (colores, tipografias y sombras).
3. Routing inicial con shell y rutas de navegacion principales.
4. Layout principal profesional con header persistente y contenido por vista.
5. Estructura base `core/shared/features` para escalar por subtareas.

## Estructura creada

- `frontend/src/app/core/layout`
- `frontend/src/app/shared/ui/page-placeholder`
- `frontend/src/app/features/home/pages`
- `frontend/src/app/features/auth/pages`
- `frontend/src/app/features/catalog/pages`
- `frontend/src/app/features/cart/pages`
- `frontend/src/app/features/checkout/pages`
- `frontend/src/app/features/tracking/pages`

## Evidencia tecnica

Archivos principales:

- `frontend/tailwind.config.js`
- `frontend/src/styles.scss`
- `frontend/src/app/app.routes.ts`
- `frontend/src/app/core/layout/app-shell.component.ts`
- `frontend/src/app/core/layout/app-shell.component.html`

## Validacion ejecutada

- `frontend` `npm run build`: verde.
- `frontend` `npm run test -- --watch=false --browsers=ChromeHeadless`: verde (2 tests, 0 failures).

## Definition of Done

- `ng serve` levanta sin errores.
- Tailwind aplica estilos en componentes reales.
- Rutas base de la epica disponibles desde shell.
- Estructura de proyecto lista para KAN-19, KAN-20, KAN-30, KAN-31 y KAN-32.
