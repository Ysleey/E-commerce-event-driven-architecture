import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiClientService } from '../../../core/http/services/api-client.service';
import { TrackingOrderResponse, TrackingQueryMode } from '../models/tracking-order.model';

@Injectable({ providedIn: 'root' })
export class TrackingOrderService {
  constructor(private readonly apiClient: ApiClientService) {}

  findOrder(mode: TrackingQueryMode, value: string): Observable<TrackingOrderResponse> {
    const normalized = encodeURIComponent(value.trim());
    if (mode === 'trackingNumber') {
      return this.apiClient.get<TrackingOrderResponse>(`/api/orders/by-tracking-number/${normalized}`);
    }

    return this.apiClient.get<TrackingOrderResponse>(`/api/orders/by-order-number/${normalized}`);
  }
}
