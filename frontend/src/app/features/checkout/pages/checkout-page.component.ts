import { Component } from '@angular/core';
import { PagePlaceholderComponent } from '../../../shared/ui/page-placeholder/page-placeholder.component';

@Component({
  selector: 'app-checkout-page',
  standalone: true,
  imports: [PagePlaceholderComponent],
  template: `
    <app-page-placeholder
      title="Checkout y confirmacion"
      description="En este punto conectaremos el formulario de compra al endpoint de pedidos y feedback transaccional al usuario."
    />
  `,
})
export class CheckoutPageComponent {}
