export interface TrackingOrderResponse {
  id: number;
  orderNumber: string;
  status: string;
  invoiceNumber: string;
  notes: string;
  updatedAt: string;
  createdAt: string;
  shippingInfo?: {
    trackingNumber?: string;
    shippingAddress?: string;
    billingAddress?: string;
    shippingMethod?: string;
    paymentMethod?: string;
  };
}

export type TrackingQueryMode = 'orderNumber' | 'trackingNumber';
