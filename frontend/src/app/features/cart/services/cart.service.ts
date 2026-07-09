import { Injectable, computed, signal } from '@angular/core';
import { Product } from '../../catalog/models/product.model';
import { CartItem } from '../models/cart-item.model';

@Injectable({ providedIn: 'root' })
export class CartService {
  private readonly itemsSignal = signal<CartItem[]>([]);

  readonly items = computed(() => this.itemsSignal());
  readonly totalItems = computed(() => this.itemsSignal().reduce((acc, item) => acc + item.quantity, 0));
  readonly subtotal = computed(() => this.round(this.itemsSignal().reduce((acc, item) => acc + item.product.price * item.quantity, 0)));
  readonly tax = computed(() => this.round(this.subtotal() * 0.21));
  readonly shipping = computed(() => (this.subtotal() >= 120 || this.subtotal() === 0 ? 0 : 6.99));
  readonly total = computed(() => this.round(this.subtotal() + this.tax() + this.shipping()));
  readonly isEmpty = computed(() => this.itemsSignal().length === 0);

  addProduct(product: Product): void {
    this.itemsSignal.update((items) => {
      const existing = items.find((item) => item.product.id === product.id);
      if (!existing) {
        return [...items, { product, quantity: 1 }];
      }

      return items.map((item) =>
        item.product.id === product.id
          ? {
              ...item,
              quantity: Math.min(item.quantity + 1, item.product.stock),
            }
          : item,
      );
    });
  }

  setQuantity(productId: number, quantity: number): void {
    this.itemsSignal.update((items) => {
      if (quantity <= 0) {
        return items.filter((item) => item.product.id !== productId);
      }

      return items.map((item) =>
        item.product.id === productId
          ? {
              ...item,
              quantity: Math.min(quantity, item.product.stock),
            }
          : item,
      );
    });
  }

  removeProduct(productId: number): void {
    this.itemsSignal.update((items) => items.filter((item) => item.product.id !== productId));
  }

  clear(): void {
    this.itemsSignal.set([]);
  }

  private round(value: number): number {
    return Math.round(value * 100) / 100;
  }
}
