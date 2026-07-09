import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-page-placeholder',
  standalone: true,
  imports: [CommonModule],
  template: `
    <section class="rounded-3xl border border-line bg-card p-6 shadow-soft sm:p-8">
      <p class="mb-4 inline-flex rounded-full bg-brand-900/60 px-3 py-1 text-xs font-bold uppercase tracking-[0.15em] text-brand-100">
        KAN-18 Base Ready
      </p>
      <h1 class="font-display text-3xl font-bold text-ink sm:text-4xl">{{ title }}</h1>
      <p class="mt-3 max-w-2xl text-base leading-relaxed text-muted">{{ description }}</p>
    </section>
  `,
})
export class PagePlaceholderComponent {
  @Input({ required: true }) title = '';
  @Input({ required: true }) description = '';
}
