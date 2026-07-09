import { Component } from '@angular/core';
import { PagePlaceholderComponent } from '../../../shared/ui/page-placeholder/page-placeholder.component';

@Component({
  selector: 'app-catalog-page',
  standalone: true,
  imports: [PagePlaceholderComponent],
  template: `
    <app-page-placeholder
      title="Catalogo de productos"
      description="Vista base para listar productos y conectar acciones de compra en la siguiente iteracion de la epica."
    />
  `,
})
export class CatalogPageComponent {}
