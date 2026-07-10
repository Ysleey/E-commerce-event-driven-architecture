# Postman

Esta carpeta contiene el paquete importable para validar el backend con Postman.

## Archivos

- [Coleccion Postman](ecommerce-app.postman_collection.json)
- [Environment Postman](ecommerce-app.postman_environment.json)

## Uso

1. Importa primero el environment.
2. Importa la collection.
3. Verifica que `orderServiceUrl` y `shippingServiceUrl` apunten a los puertos locales correctos.
4. Ejecuta `Login admin` para guardar `accessToken`.
5. Ejecuta el bloque de pruebas de ordenes y health checks.

## Cobertura funcional

- Autenticacion JWT.
- Creacion y consulta de ordenes.
- Actualizacion de direccion de envio.
- Transicion de estado de orden.
- Health checks y endpoints operativos de ambos servicios.
