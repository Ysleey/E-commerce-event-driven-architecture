import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-price-breakdown',
  standalone: true,
  imports: [CommonModule],
  template: `
    <h3 *ngIf="title" class="font-display text-xl font-bold text-ink">{{ title }}</h3>

    <dl class="mt-4 space-y-3 text-sm">
      <div class="flex items-center justify-between text-muted">
        <dt>Items</dt>
        <dd>{{ items }}</dd>
      </div>
      <div class="flex items-center justify-between text-muted">
        <dt>Subtotal</dt>
        <dd>{{ subtotal | currency:'EUR' }}</dd>
      </div>
      <div class="flex items-center justify-between text-muted">
        <dt>Impuestos</dt>
        <dd>{{ tax | currency:'EUR' }}</dd>
      </div>
      <div class="flex items-center justify-between text-muted">
        <dt>Envio</dt>
        <dd>{{ shipping | currency:'EUR' }}</dd>
      </div>
      <div class="mt-3 flex items-center justify-between border-t border-line pt-3">
        <dt class="font-display text-lg font-bold text-ink">Total</dt>
        <dd class="font-display text-2xl font-bold text-brand-200">{{ total | currency:'EUR' }}</dd>
      </div>
    </dl>
  `,
})
export class PriceBreakdownComponent {
  @Input() title = '';
  @Input({ required: true }) items = 0;
  @Input({ required: true }) subtotal = 0;
  @Input({ required: true }) tax = 0;
  @Input({ required: true }) shipping = 0;
  @Input({ required: true }) total = 0;
}
