import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-home-page',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './home-page.component.html',
})
export class HomePageComponent {
  readonly highlights = [
    {
      title: 'Arquitectura preparada',
      description: 'Base en features, capa core y componentes shared para escalar sin deuda tecnica.',
    },
    {
      title: 'Integracion backend-first',
      description: 'Flujo orientado a contrato OpenAPI real para evitar desviaciones entre frontend y API.',
    },
    {
      title: 'Calidad desde el inicio',
      description: 'Estructura lista para pruebas unitarias, CI y evidencia profesional de portafolio.',
    },
  ];
}
