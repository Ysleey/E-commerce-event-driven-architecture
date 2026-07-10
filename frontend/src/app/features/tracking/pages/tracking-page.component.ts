import { CommonModule } from '@angular/common';
import { Component, OnDestroy } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ViewportScroller } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { finalize } from 'rxjs/operators';
import { ApiError } from '../../../core/http/models/api-error.model';
import { OrderHistoryEntry } from '../../checkout/models/order.models';
import { OrderHistoryService } from '../../checkout/services/order-history.service';
import { TrackingOrderResponse, TrackingQueryMode } from '../models/tracking-order.model';
import { TrackingOrderService } from '../services/tracking-order.service';

interface TrackingStep {
  key: string;
  label: string;
  reached: boolean;
}

@Component({
  selector: 'app-tracking-page',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './tracking-page.component.html',
})
export class TrackingPageComponent implements OnDestroy {
  queryMode: TrackingQueryMode = 'orderNumber';
  queryValue = '';
  autoRefreshEnabled = true;
  isLoading = false;
  isRefreshing = false;
  errorMessage = '';
  infoMessage = '';
  lastUpdated: Date | null = null;
  order: TrackingOrderResponse | null = null;
  historyEntry: OrderHistoryEntry | null = null;
  refreshMessage = '';

  private pollingHandle: ReturnType<typeof setInterval> | null = null;

  constructor(
    private readonly trackingService: TrackingOrderService,
    private readonly route: ActivatedRoute,
    private readonly router: Router,
    private readonly viewportScroller: ViewportScroller,
    readonly orderHistory: OrderHistoryService,
  ) {
    this.hydrateFromQueryParams();
  }

  ngOnDestroy(): void {
    this.stopAutoRefresh();
  }

  submitSearch(): void {
    this.syncUrl();
    this.fetchOrder({ clearPrevious: true, background: false });
  }

  refreshNow(): void {
    const lookupValue = this.queryMode === 'trackingNumber'
      ? (this.order?.shippingInfo?.trackingNumber ?? this.queryValue.trim())
      : (this.order?.orderNumber ?? this.queryValue.trim());

    if (!lookupValue) {
      this.errorMessage = 'No hay identificador disponible para refrescar.';
      return;
    }

    this.queryValue = '';
    this.syncUrl();
    this.refreshMessage = 'Actualizando estado demo del pedido...';
    this.fetchOrder({ clearPrevious: false, background: true, lookupValue });
  }

  onToggleAutoRefresh(): void {
    if (!this.autoRefreshEnabled) {
      this.stopAutoRefresh();
      return;
    }

    if (this.order) {
      this.startAutoRefresh();
    }
  }

  get trackingSteps(): TrackingStep[] {
    if (!this.order) {
      return [];
    }

    const status = this.order.status?.toUpperCase() ?? 'CREATED';
    const baseFlow = ['CREATED', 'SHIPPED', 'COMPLETED'];
    const labels: Record<string, string> = {
      CREATED: 'Pedido creado',
      SHIPPED: 'Pedido enviado',
      COMPLETED: 'Pedido completado',
    };

    const progressIndex = baseFlow.indexOf(status);
    return baseFlow.map((key, index) => {
      const reached = progressIndex >= index || (status !== 'SHIPPED' && status !== 'COMPLETED' && key === 'CREATED');
      return {
        key,
        label: labels[key],
        reached,
      };
    });
  }

  statusBadgeClass(status: string | undefined): string {
    const normalized = (status ?? '').toUpperCase();
    if (normalized === 'COMPLETED') {
      return 'border-success/60 bg-success/15 text-[#d6efc7]';
    }
    if (normalized === 'SHIPPED') {
      return 'border-brand-700/60 bg-brand-900/40 text-brand-100';
    }
    if (normalized === 'CANCELLED') {
      return 'border-danger/60 bg-danger/15 text-[#ffc6be]';
    }
    if (normalized === 'RETURN_REQUESTED' || normalized === 'REFUND_REQUESTED') {
      return 'border-brand-500/60 bg-brand-900/35 text-brand-200';
    }
    return 'border-line bg-surface text-muted';
  }

  private fetchOrder(options: { clearPrevious: boolean; background: boolean; lookupValue?: string }): void {
    const value = (options.lookupValue ?? this.queryValue).trim();
    if (!value) {
      this.errorMessage = 'Introduce un numero de pedido o tracking para buscar.';
      return;
    }

    if (options.clearPrevious) {
      this.order = null;
      this.historyEntry = null;
      this.lastUpdated = null;
      this.stopAutoRefresh();
    }

    this.errorMessage = '';
    this.infoMessage = '';
    this.refreshMessage = '';
    this.isLoading = !options.background;
    this.isRefreshing = options.background;

    this.trackingService
      .findOrder(this.queryMode, value)
      .pipe(
        finalize(() => {
          this.isLoading = false;
          this.isRefreshing = false;
        }),
      )
      .subscribe({
        next: (order) => {
          const matchedHistoryEntry = options.background ? this.advanceHistoryEntry() : this.resolveHistoryEntry(order);
          this.historyEntry = matchedHistoryEntry;
          this.order = matchedHistoryEntry
            ? {
                ...order,
                status: matchedHistoryEntry.status,
              }
            : order;
          this.lastUpdated = new Date();
          this.viewportScroller.scrollToPosition([0, 0]);
          if (options.background) {
            this.refreshMessage = this.historyEntry
              ? `Estado actualizado a ${this.historyEntry.status}.`
              : 'Se refresco el estado del pedido.';
          }
          if (this.autoRefreshEnabled) {
            this.startAutoRefresh();
          }
        },
        error: (error: ApiError) => {
          this.order = null;
          this.historyEntry = null;
          this.lastUpdated = null;
          this.refreshMessage = '';
          if (error.kind === 'not-found') {
            this.infoMessage = 'No se encontro un pedido con ese identificador.';
            return;
          }
          this.errorMessage = error.message;
        },
      });
  }

  private startAutoRefresh(): void {
    this.stopAutoRefresh();
    this.pollingHandle = setInterval(() => {
      if (!this.order || this.isLoading || this.isRefreshing) {
        return;
      }
      const lookupValue = this.queryMode === 'trackingNumber'
        ? (this.order?.shippingInfo?.trackingNumber ?? '')
        : this.order.orderNumber;
      if (!lookupValue) {
        return;
      }
      this.fetchOrder({ clearPrevious: false, background: true, lookupValue });
    }, 10_000);
  }

  private stopAutoRefresh(): void {
    if (!this.pollingHandle) {
      return;
    }

    clearInterval(this.pollingHandle);
    this.pollingHandle = null;
  }

  private hydrateFromQueryParams(): void {
    const mode = this.route.snapshot.queryParamMap.get('mode');
    const value = this.route.snapshot.queryParamMap.get('value');
    const autoSearch = this.route.snapshot.queryParamMap.get('autoSearch');

    if (mode === 'orderNumber' || mode === 'trackingNumber') {
      this.queryMode = mode;
    }

    if (!value) {
      return;
    }

    this.queryValue = value;
    if (autoSearch === '1') {
      this.submitSearch();
    }
  }

  private syncUrl(): void {
    void this.router.navigate([], {
      relativeTo: this.route,
      queryParams: {
        mode: this.queryMode,
        value: this.queryValue.trim(),
        autoSearch: '1',
      },
      replaceUrl: true,
    });
  }

  private resolveHistoryEntry(order: TrackingOrderResponse): OrderHistoryEntry | null {
    const trackingNumber = order.shippingInfo?.trackingNumber;
    if (trackingNumber) {
      const matchByTracking = this.orderHistory.findByTrackingNumber(trackingNumber);
      if (matchByTracking) {
        return matchByTracking;
      }
    }

    return this.orderHistory.findByOrderNumber(order.orderNumber);
  }

  private advanceHistoryEntry(): OrderHistoryEntry | null {
    const value = this.queryMode === 'trackingNumber'
      ? (this.order?.shippingInfo?.trackingNumber ?? this.queryValue.trim())
      : (this.order?.orderNumber ?? this.queryValue.trim());
    if (!value) {
      return null;
    }

    return this.queryMode === 'trackingNumber'
      ? this.orderHistory.advanceStatusByTrackingNumber(value)
      : this.orderHistory.advanceStatusByOrderNumber(value);
  }
}
