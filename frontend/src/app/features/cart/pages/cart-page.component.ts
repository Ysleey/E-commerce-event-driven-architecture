import { Component } from '@angular/core';
import { PagePlaceholderComponent } from '../../../shared/ui/page-placeholder/page-placeholder.component';

@Component({
  selector: 'app-cart-page',
  standalone: true,
  imports: [PagePlaceholderComponent],
  template: `
    <app-page-placeholder
      title="Carrito de compras"
      description="Base para gestionar items, cantidades, subtotal y validaciones de compra antes de checkout."
    />
  `,
})
export class CartPageComponent {}
