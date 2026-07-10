import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { finalize } from 'rxjs/operators';
import { ApiError } from '../../../core/http/models/api-error.model';
import { EmptyStateCardComponent } from '../../../shared/ui/empty-state-card/empty-state-card.component';
import { FeedbackAlertComponent } from '../../../shared/ui/feedback-alert/feedback-alert.component';
import { PriceBreakdownComponent } from '../../../shared/ui/price-breakdown/price-breakdown.component';
import { CartItem } from '../../cart/models/cart-item.model';
import { CartService } from '../../cart/services/cart.service';
import { CreateOrderRequest, OrderHistoryEntry, OrderResponse, PurchasedOrderItem } from '../models/order.models';
import { CheckoutOrderService } from '../services/checkout-order.service';
import { OrderHistoryService } from '../services/order-history.service';

@Component({
  selector: 'app-checkout-page',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, EmptyStateCardComponent, FeedbackAlertComponent, PriceBreakdownComponent],
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
  notes = 'Pedido generado desde frontend Angular';
  isSubmitting = false;
  errorMessage = '';
  successMessage = '';
  createdOrder: OrderResponse | null = null;
  purchasedItems: PurchasedOrderItem[] = [];
  purchasedSubtotal = 0;
  purchasedTax = 0;
  purchasedShipping = 0;
  purchasedTotal = 0;

  constructor(
    readonly cart: CartService,
    private readonly checkoutOrderService: CheckoutOrderService,
    readonly orderHistory: OrderHistoryService,
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
          this.capturePurchaseSummary(this.cart.items());
          this.createdOrder = order;
          this.orderHistory.save(this.buildOrderHistoryEntry(order));
          this.successMessage = `Pedido ${order.orderNumber} creado correctamente.`;
          this.cart.clear();
        },
        error: (error: ApiError) => {
          this.errorMessage = error.message;
        },
      });
  }

  get hasCompletedOrder(): boolean {
    return this.createdOrder !== null;
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

  private capturePurchaseSummary(items: CartItem[]): void {
    this.purchasedItems = items.map((item) => ({
      name: item.product.name,
      quantity: item.quantity,
      unitPrice: item.product.price,
      lineTotal: Math.round(item.product.price * item.quantity * 100) / 100,
    }));
    this.purchasedSubtotal = this.cart.subtotal();
    this.purchasedTax = this.cart.tax();
    this.purchasedShipping = this.cart.shipping();
    this.purchasedTotal = this.cart.total();
  }

  private buildOrderHistoryEntry(order: OrderResponse): OrderHistoryEntry {
    return {
      id: order.id,
      orderNumber: order.orderNumber,
      trackingNumber: order.shippingInfo.trackingNumber,
      status: order.status,
      invoiceNumber: order.invoiceNumber,
      createdAt: order.createdAt,
      paymentMethod: this.paymentMethod,
      shippingMethod: this.shippingMethod,
      shippingAddress: this.shippingAddress,
      billingAddress: this.billingAddress,
      customerName: this.customerName,
      customerEmail: this.customerEmail,
      totalItems: this.purchasedItems.reduce((acc, item) => acc + item.quantity, 0),
      subtotal: this.purchasedSubtotal,
      tax: this.purchasedTax,
      shipping: this.purchasedShipping,
      total: this.purchasedTotal,
      notes: this.notes,
      items: this.purchasedItems,
    };
  }
}
