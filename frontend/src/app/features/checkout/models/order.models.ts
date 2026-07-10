export interface CreateOrderRequest {
  customerId: number;
  productId: number;
  paymentId: number;
  orderNumber: string;
  invoiceNumber: string;
  notes: string;
  customerContact: {
    customerName: string;
    customerEmail: string;
    customerPhone: string;
  };
  financials: {
    price: number;
    discountAmount: number;
    taxAmount: number;
    shippingAmount: number;
    totalAmount: number;
    couponCode: string;
    currency: string;
  };
  shippingInfo: {
    paymentMethod: string;
    shippingMethod: string;
    trackingNumber: string;
    shippingAddress: string;
    billingAddress: string;
  };
}

export interface OrderResponse {
  id: number;
  orderNumber: string;
  status: string;
  invoiceNumber: string;
  createdAt: string;
  shippingInfo: {
    trackingNumber: string;
  };
}

export interface PurchasedOrderItem {
  name: string;
  quantity: number;
  unitPrice: number;
  lineTotal: number;
}

export interface OrderHistoryEntry {
  id: number;
  orderNumber: string;
  trackingNumber: string;
  status: string;
  invoiceNumber: string;
  createdAt: string;
  paymentMethod: string;
  shippingMethod: string;
  shippingAddress: string;
  billingAddress: string;
  customerName: string;
  customerEmail: string;
  totalItems: number;
  subtotal: number;
  tax: number;
  shipping: number;
  total: number;
  notes: string;
  items: PurchasedOrderItem[];
}
