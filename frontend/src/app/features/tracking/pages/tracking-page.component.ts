import { CommonModule } from '@angular/common';
import { Component, OnDestroy } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { finalize } from 'rxjs/operators';
import { ApiError } from '../../../core/http/models/api-error.model';
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

  private pollingHandle: ReturnType<typeof setInterval> | null = null;

  constructor(private readonly trackingService: TrackingOrderService) {}

  ngOnDestroy(): void {
    this.stopAutoRefresh();
  }

  submitSearch(): void {
    this.fetchOrder({ clearPrevious: true, background: false });
  }

  refreshNow(): void {
    this.fetchOrder({ clearPrevious: false, background: true });
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

  private fetchOrder(options: { clearPrevious: boolean; background: boolean }): void {
    const value = this.queryValue.trim();
    if (!value) {
      this.errorMessage = 'Introduce un numero de pedido o tracking para buscar.';
      return;
    }

    if (options.clearPrevious) {
      this.order = null;
      this.lastUpdated = null;
      this.stopAutoRefresh();
    }

    this.errorMessage = '';
    this.infoMessage = '';
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
          this.order = order;
          this.lastUpdated = new Date();
          if (this.autoRefreshEnabled) {
            this.startAutoRefresh();
          }
        },
        error: (error: ApiError) => {
          this.order = null;
          this.lastUpdated = null;
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
      this.fetchOrder({ clearPrevious: false, background: true });
    }, 10_000);
  }

  private stopAutoRefresh(): void {
    if (!this.pollingHandle) {
      return;
    }

    clearInterval(this.pollingHandle);
    this.pollingHandle = null;
  }
}
