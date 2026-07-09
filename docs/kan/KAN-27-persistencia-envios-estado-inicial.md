# KAN-27 - Persistencia de envios y estado inicial

## Objetivo

Persistir el envio en shipping-service desde un `OrderCreated` con estado inicial `PENDING` y regla clara de unicidad por `orderId`.

## Alcance implementado

1. Entidad `Shipping` persistida en tabla `shippings`.
2. Regla de unicidad por `order_id` para evitar dos envios para la misma orden.
3. Caso de uso de creacion de envio con estado inicial `PENDING`.
4. Adapter de persistencia JPA para guardar y recuperar el estado de negocio.

## Modelo de datos

Tabla principal:

```sql
shippings (
  id UUID PK,
  order_id UUID NOT NULL,
  status VARCHAR(30) NOT NULL,
  tracking_number VARCHAR(255),
  created_at TIMESTAMP WITH TIME ZONE NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
  UNIQUE(order_id)
)
```

## Regla funcional

1. Un `OrderCreated` origina un envio nuevo.
2. El envio nace en `PENDING`.
3. El campo `trackingNumber` queda vacío hasta que exista asignación logística.
4. La unicidad por `orderId` evita duplicados de negocio.

## Evidencia tecnica

Archivos principales:

- `shipping-service/src/main/java/com/ecommerce/shipping/domain/model/Shipping.java`
- `shipping-service/src/main/java/com/ecommerce/shipping/adapter/out/persistence/ShippingEntity.java`
- `shipping-service/src/main/java/com/ecommerce/shipping/adapter/out/persistence/ShippingPersistenceAdapter.java`
- `shipping-service/src/main/java/com/ecommerce/shipping/application/service/ShippingUseCaseService.java`

Pruebas:

- `shipping-service/src/test/java/com/ecommerce/shipping/application/service/ShippingUseCaseServiceTest.java`
- `shipping-service/src/test/java/com/ecommerce/shipping/adapter/out/persistence/ShippingPersistenceAdapterTest.java`

## Definition of Done

- Estado inicial del envio definido como `PENDING`.
- Persistencia JPA validada.
- Regla de unicidad por orden documentada.
- Tests del caso de uso y persistencia en verde.
