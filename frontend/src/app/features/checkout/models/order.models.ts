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
