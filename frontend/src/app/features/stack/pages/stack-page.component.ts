import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';

@Component({
  selector: 'app-stack-page',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './stack-page.component.html',
})
export class StackPageComponent {
  activeStackIndex = 0;
  activeKpiIndex: number | null = null;

  readonly stackCards = [
    {
      title: 'Frontend',
      detail: 'Angular standalone, rutas protegidas y estado reactivo para carrito y flujo de compra.',
      tech: 'Angular 17 + Tailwind',
      highlights: [
        'Rutas protegidas por autenticacion JWT',
        'Flujo completo catalogo -> carrito -> checkout -> tracking',
        'UI responsive preparada para demo tecnica y funcional',
      ],
    },
    {
      title: 'Order Service',
      detail: 'API REST para pedidos con seguridad JWT y contratos de integracion versionados.',
      tech: 'Spring Boot + Spring Security',
      highlights: [
        'Creacion y postventa de pedidos con endpoints versionados',
        'Validaciones de negocio y manejo de errores consistente',
        'Publicacion de eventos para desacoplar procesos logisticos',
      ],
    },
    {
      title: 'Shipping Service',
      detail: 'Consumo asincrono de eventos para evolucion del estado logistico de cada pedido.',
      tech: 'Spring Boot + Kafka Listener',
      highlights: [
        'Consumo de eventos order-created y evolucion de estados',
        'Modelo idempotente para evitar reprocesamientos',
        'Exposicion de tracking por numero de pedido y tracking id',
      ],
    },
    {
      title: 'Mensajeria y datos',
      detail: 'Comunicacion por eventos y persistencia desacoplada para escalar servicios por separado.',
      tech: 'Apache Kafka + PostgreSQL',
      highlights: [
        'Contrato de eventos versionado para evolucion segura',
        'Persistencia aislada por microservicio para independencia',
        'Trazabilidad end-to-end con correlation id y logs tecnicos',
      ],
    },
  ];

  readonly stackKpis = [
    {
      title: '2 microservicios activos',
      description: 'Order y Shipping operando con contratos claros.',
      details: ['order-service', 'shipping-service'],
    },
    {
      title: 'Smoke tests en verde',
      description: 'Flujo de autenticacion y ciclo de vida validado en Postman.',
      details: ['auth login', 'create order', 'tracking por order number', 'tracking por tracking number'],
    },
    {
      title: 'Trazabilidad end-to-end',
      description: 'CorrelationId propagado en API, logs y eventos.',
      details: ['request id en order-service', 'evento order created en kafka', 'consumer en shipping-service'],
    },
    {
      title: 'Contratos versionados',
      description: 'OpenAPI y eventos Kafka gobernados para evolucion segura.',
      details: ['OpenAPI order-service v1', 'Kafka event contract v1', 'reglas de versionado API y eventos'],
    },
  ];

  get activeStackCard() {
    return this.stackCards[this.activeStackIndex] ?? this.stackCards[0];
  }

  setActiveStack(index: number): void {
    this.activeStackIndex = index;
  }

  setActiveKpi(index: number | null): void {
    this.activeKpiIndex = index;
  }
}