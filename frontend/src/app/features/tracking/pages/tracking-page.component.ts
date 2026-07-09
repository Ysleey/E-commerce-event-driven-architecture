import { Component } from '@angular/core';
import { PagePlaceholderComponent } from '../../../shared/ui/page-placeholder/page-placeholder.component';

@Component({
  selector: 'app-tracking-page',
  standalone: true,
  imports: [PagePlaceholderComponent],
  template: `
    <app-page-placeholder
      title="Seguimiento asincrono"
      description="Vista preparada para reflejar el estado del pedido y su evolucion logistica conforme a eventos del backend."
    />
  `,
})
export class TrackingPageComponent {}
