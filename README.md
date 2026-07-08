# E-commerce-event-driven-architecture
Microservicios e-commerce con Arquitectura Hexagonal, Spring Boot, Apache Kafka y Angular.

## Arquitectura objetivo

Este proyecto está pensado como una demostración profesional de arquitectura hexagonal aplicada a un e-commerce distribuido, con backend en Java y Spring Boot, mensajería asíncrona con Apache Kafka, persistencia relacional con PostgreSQL y frontend en Angular.

### Objetivo técnico

Construir una plataforma evolutiva que permita mostrar:

- Arquitectura Hexagonal con separación clara entre dominio, aplicación y adaptadores.
- Microservicios desacoplados, con comunicación síncrona por REST y asíncrona por Kafka.
- Persistencia relacional por servicio con PostgreSQL en Docker.
- Seguridad con JWT.
- Frontend Angular consumiendo contratos de API bien definidos.
- Calidad de código, trazabilidad, documentación y observabilidad básica.

### Estructura objetivo del proyecto

```text
com.ecommerce.order/
├── adapter/
│   ├── in/
│   │   ├── controller/
│   │   └── messaging/
│   └── out/
│       ├── persistence/
│       └── messaging/
├── application/
├── domain/
└── ports/
```

### Tecnologías previstas

- Java 17+
- Spring Boot
- Maven
- Apache Kafka
- PostgreSQL
- Docker y Docker Compose
- JWT
- Angular 17+
- Tailwind CSS

### Contratos del proyecto

- [OpenAPI v1 del order-service](docs/contracts/order-service-openapi-v1.yaml)
- [Contrato de eventos Kafka v1](docs/contracts/kafka-events-v1.md)

### Plan de evolución

1. Definir contratos de API y eventos.
2. Completar el backend principal del order-service.
3. Añadir seguridad JWT y mensajería Kafka.
4. Construir el shipping-service como microservicio independiente.
5. Desarrollar el frontend Angular.
6. Consolidar documentación técnica, calidad y CI.

### Organización en Jira

El trabajo se gestionará por épicas y subtareas para mantener control de avance, definición clara de entregables y evidencia técnica de cada fase.

### Flujo de trabajo acordado

1. Definir primero el objetivo de arquitectura y el alcance de cada épica o subtarea en Jira.
2. Implementar solo la subtarea activa, validar el cambio y dejar evidencia técnica antes de avanzar a la siguiente.
3. Cuando se complete una fase importante, preparar un push a `develop` desde GitHub Desktop con un mensaje claro y descriptivo.

### Cierre de subtareas en Jira

Cuando completes una subtarea, el orden recomendado será:

1. Verificar que el cambio cumple la descripción y el DoD de la subtarea.
2. Confirmar que el proyecto compila y, si aplica, que los tests relevantes pasan.
3. Actualizar la subtarea en Jira con una nota breve de lo realizado y la evidencia.
4. Marcar la subtarea como finalizada solo cuando el resultado sea reproducible.

### Cuándo hacer push a develop

Haz push a `develop` cuando completes una fase importante, por ejemplo:

1. Finalización de una épica o bloque funcional completo.
2. Cierre de un contrato importante, como API OpenAPI o esquema de eventos Kafka.
3. Terminación de una base técnica estable, como seguridad, persistencia o mensajería.

### Mensajes sugeridos para push

Usa mensajes claros y orientados al resultado, por ejemplo:

- `feat(order-service): add OpenAPI contract and REST entry structure`
- `feat(kafka): define order events contract and metadata`
- `feat(shipping-service): add consumer flow and persistence foundation`
- `docs: update architecture and project roadmap`
- `chore: reorganize hexagonal structure and clean packages`

### Herramientas de apoyo

- Docker Desktop para levantar PostgreSQL y Kafka localmente.
- GitHub Desktop para revisar cambios, hacer commit y preparar push a `develop`.
- Git Bash o terminal para validaciones rápidas si necesitas ejecutar comandos manuales.
