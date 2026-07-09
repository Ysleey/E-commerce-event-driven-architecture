import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { EmptyStateCardComponent } from '../../../shared/ui/empty-state-card/empty-state-card.component';
import { PriceBreakdownComponent } from '../../../shared/ui/price-breakdown/price-breakdown.component';
import { CartService } from '../services/cart.service';

@Component({
  selector: 'app-cart-page',
  standalone: true,
  imports: [CommonModule, RouterLink, EmptyStateCardComponent, PriceBreakdownComponent],
  templateUrl: './cart-page.component.html',
})
export class CartPageComponent {
  constructor(readonly cart: CartService) {}

  decrease(productId: number, currentQty: number): void {
    this.cart.setQuantity(productId, currentQty - 1);
  }

  increase(productId: number, currentQty: number): void {
    this.cart.setQuantity(productId, currentQty + 1);
  }

  remove(productId: number): void {
    this.cart.removeProduct(productId);
  }

  clear(): void {
    this.cart.clear();
  }
}
