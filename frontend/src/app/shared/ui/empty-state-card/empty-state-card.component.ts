import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-empty-state-card',
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <section class="rounded-3xl border border-line bg-card p-8 text-center shadow-soft">
      <h2 class="font-display text-2xl font-bold text-ink">{{ title }}</h2>
      <p class="mt-2 text-sm text-muted">{{ description }}</p>
      <a
        *ngIf="actionLabel && actionLink"
        [routerLink]="actionLink"
        class="mt-5 inline-block rounded-xl bg-brand-400 px-5 py-2.5 text-sm font-bold text-[#1e1510] transition hover:bg-brand-300"
      >
        {{ actionLabel }}
      </a>
    </section>
  `,
})
export class EmptyStateCardComponent {
  @Input({ required: true }) title = '';
  @Input({ required: true }) description = '';
  @Input() actionLabel = '';
  @Input() actionLink = '';
}
