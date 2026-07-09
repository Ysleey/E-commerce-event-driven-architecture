import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { finalize } from 'rxjs/operators';
import { ApiError } from '../../../core/http/models/api-error.model';
import { CartService } from '../../cart/services/cart.service';
import { CreateOrderRequest, OrderResponse } from '../models/order.models';
import { CheckoutOrderService } from '../services/checkout-order.service';

@Component({
  selector: 'app-checkout-page',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './checkout-page.component.html',
})
export class CheckoutPageComponent {
  customerName = 'Nico Portfolio';
  customerEmail = 'nico@example.com';
  customerPhone = '+34 600 123 456';
  shippingAddress = 'Calle Mayor 1, Madrid';
  billingAddress = 'Calle Mayor 1, Madrid';
  paymentMethod = 'CARD';
  shippingMethod = 'STANDARD';
  notes = 'Pedido generado desde frontend Angular KAN-20';
  isSubmitting = false;
  errorMessage = '';
  successMessage = '';
  createdOrder: OrderResponse | null = null;

  constructor(
    readonly cart: CartService,
    private readonly checkoutOrderService: CheckoutOrderService,
  ) {}

  submitOrder(): void {
    this.errorMessage = '';
    this.successMessage = '';

    const payload = this.buildPayload();
    if (!payload) {
      this.errorMessage = 'No hay productos en el carrito para crear el pedido.';
      return;
    }

    this.isSubmitting = true;
    this.checkoutOrderService
      .createOrder(payload)
      .pipe(finalize(() => (this.isSubmitting = false)))
      .subscribe({
        next: (order) => {
          this.createdOrder = order;
          this.successMessage = `Pedido ${order.orderNumber} creado correctamente.`;
          this.cart.clear();
        },
        error: (error: ApiError) => {
          this.errorMessage = error.message;
        },
      });
  }

  private buildPayload(): CreateOrderRequest | null {
    const items = this.cart.items();
    if (items.length === 0) {
      return null;
    }

    const firstItem = items[0];
    const now = Date.now();
    const orderNumber = `ORD-FE-${now}`;
    const invoiceNumber = `INV-FE-${now}`;
    const trackingNumber = `TRK-FE-${now.toString().slice(-8)}`;
    const itemSummary = items.map((item) => `${item.product.name} x${item.quantity}`).join(', ');

    return {
      customerId: 1001,
      productId: firstItem.product.id,
      paymentId: 7001,
      orderNumber,
      invoiceNumber,
      notes: `${this.notes}. Items: ${itemSummary}`,
      customerContact: {
        customerName: this.customerName,
        customerEmail: this.customerEmail,
        customerPhone: this.customerPhone,
      },
      financials: {
        price: this.cart.subtotal(),
        discountAmount: 0,
        taxAmount: this.cart.tax(),
        shippingAmount: this.cart.shipping(),
        totalAmount: this.cart.total(),
        couponCode: '',
        currency: 'EUR',
      },
      shippingInfo: {
        paymentMethod: this.paymentMethod,
        shippingMethod: this.shippingMethod,
        trackingNumber,
        shippingAddress: this.shippingAddress,
        billingAddress: this.billingAddress,
      },
    };
  }
}
