# shipping-service

Microservicio base para orquestar el ciclo de vida de envios en la plataforma e-commerce.

## Objetivo de KAN-14

- Inicializar modulo independiente con Spring Boot y Maven wrapper.
- Aplicar estructura hexagonal base.
- Dejar configuracion local para PostgreSQL y Kafka.
- Validar arranque de contexto con pruebas.

## Estructura

```text
src/main/java/com/ecommerce/shipping/
|- adapter/
|  |- out/
|     |- persistence/
|- application/
|  |- service/
|- domain/
|  |- model/
|- ports/
|  |- in/
|  |- out/
```

## Ejecucion local

```bash
./mvnw spring-boot:run
```

En Windows PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```
