import { Injectable, computed, signal } from '@angular/core';
import { OrderHistoryEntry } from '../models/order.models';

const ORDER_HISTORY_STORAGE_KEY = 'ecommerce.frontend.order-history';
const MAX_HISTORY_ITEMS = 8;
const DEMO_STATUS_FLOW = ['CREATED', 'SHIPPED', 'COMPLETED'] as const;

@Injectable({ providedIn: 'root' })
export class OrderHistoryService {
  private readonly historySignal = signal<OrderHistoryEntry[]>(this.loadHistory());

  readonly history = computed(() => this.historySignal());
  readonly totalOrders = computed(() => this.historySignal().length);

  save(entry: OrderHistoryEntry): void {
    const deduped = this.historySignal().filter(
      (item) => item.orderNumber !== entry.orderNumber && item.trackingNumber !== entry.trackingNumber,
    );
    const nextHistory = [entry, ...deduped].slice(0, MAX_HISTORY_ITEMS);
    this.historySignal.set(nextHistory);
    localStorage.setItem(ORDER_HISTORY_STORAGE_KEY, JSON.stringify(nextHistory));
  }

  findByOrderNumber(orderNumber: string): OrderHistoryEntry | null {
    const normalized = orderNumber.trim().toUpperCase();
    return this.historySignal().find((item) => item.orderNumber.toUpperCase() === normalized) ?? null;
  }

  findByTrackingNumber(trackingNumber: string): OrderHistoryEntry | null {
    const normalized = trackingNumber.trim().toUpperCase();
    return this.historySignal().find((item) => item.trackingNumber.toUpperCase() === normalized) ?? null;
  }

  advanceStatusByOrderNumber(orderNumber: string): OrderHistoryEntry | null {
    const normalized = orderNumber.trim().toUpperCase();
    return this.updateMatchingEntry((item) => item.orderNumber.toUpperCase() === normalized);
  }

  advanceStatusByTrackingNumber(trackingNumber: string): OrderHistoryEntry | null {
    const normalized = trackingNumber.trim().toUpperCase();
    return this.updateMatchingEntry((item) => item.trackingNumber.toUpperCase() === normalized);
  }

  private loadHistory(): OrderHistoryEntry[] {
    const raw = localStorage.getItem(ORDER_HISTORY_STORAGE_KEY);
    if (!raw) {
      return [];
    }

    try {
      const parsed = JSON.parse(raw) as OrderHistoryEntry[];
      return Array.isArray(parsed) ? parsed : [];
    } catch {
      localStorage.removeItem(ORDER_HISTORY_STORAGE_KEY);
      return [];
    }
  }

  private updateMatchingEntry(matcher: (entry: OrderHistoryEntry) => boolean): OrderHistoryEntry | null {
    let updatedEntry: OrderHistoryEntry | null = null;
    const nextHistory = this.historySignal().map((entry) => {
      if (!matcher(entry)) {
        return entry;
      }

      const currentIndex = DEMO_STATUS_FLOW.indexOf((entry.status || 'CREATED') as (typeof DEMO_STATUS_FLOW)[number]);
      const nextIndex = currentIndex < 0 ? 0 : Math.min(currentIndex + 1, DEMO_STATUS_FLOW.length - 1);
      updatedEntry = {
        ...entry,
        status: DEMO_STATUS_FLOW[nextIndex],
      };
      return updatedEntry;
    });

    if (!updatedEntry) {
      return null;
    }

    this.historySignal.set(nextHistory);
    localStorage.setItem(ORDER_HISTORY_STORAGE_KEY, JSON.stringify(nextHistory));
    return updatedEntry;
  }
}