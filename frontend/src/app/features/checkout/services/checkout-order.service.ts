import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiClientService } from '../../../core/http/services/api-client.service';
import { CreateOrderRequest, OrderResponse } from '../models/order.models';

@Injectable({ providedIn: 'root' })
export class CheckoutOrderService {
  constructor(private readonly apiClient: ApiClientService) {}

  createOrder(payload: CreateOrderRequest): Observable<OrderResponse> {
    return this.apiClient.post<CreateOrderRequest, OrderResponse>('/api/orders', payload);
  }
}
