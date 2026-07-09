# KAN-20 - Desarrollo de Vistas de Catalogo, Carrito y Compra

## Objetivo

Implementar en Angular el flujo funcional de compra con vistas de catalogo, carrito y checkout, conectando el checkout al backend real de pedidos.

## Alcance implementado

1. Catalogo con listado de productos, filtros por categoria, precio y busqueda por texto.
2. Carrito funcional con agregar, eliminar, ajustar cantidades y resumen de totales.
3. Checkout con formulario de datos cliente/envio/pago y envio de pedido a `POST /api/orders`.
4. Mensajeria UX de exito/error y visualizacion del pedido creado.
5. Badge de cantidad de items en navegacion superior.

## Evidencia tecnica

Archivos principales:

- `frontend/src/app/features/catalog/pages/catalog-page.component.ts`
- `frontend/src/app/features/catalog/pages/catalog-page.component.html`
- `frontend/src/app/features/cart/services/cart.service.ts`
- `frontend/src/app/features/cart/pages/cart-page.component.ts`
- `frontend/src/app/features/cart/pages/cart-page.component.html`
- `frontend/src/app/features/checkout/services/checkout-order.service.ts`
- `frontend/src/app/features/checkout/pages/checkout-page.component.ts`
- `frontend/src/app/features/checkout/pages/checkout-page.component.html`
- `frontend/src/app/core/layout/app-shell.component.ts`
- `frontend/src/app/core/layout/app-shell.component.html`

Modelos y datos de soporte:

- `frontend/src/app/features/catalog/models/product.model.ts`
- `frontend/src/app/features/catalog/data/mock-products.ts`
- `frontend/src/app/features/cart/models/cart-item.model.ts`
- `frontend/src/app/features/checkout/models/order.models.ts`

## Validacion ejecutada

- `frontend` `npm run build`: verde.
- `frontend` `npm run test -- --watch=false --browsers=ChromeHeadless`: verde (5 tests, 0 failures).

## Definition of Done

- Vista de catalogo con filtros operativa.
- Carrito permite gestion completa de items y cantidades.
- Checkout envia pedido estructurado al backend y muestra confirmacion.
- Flujo visual consistente con el tema profesional del proyecto.
