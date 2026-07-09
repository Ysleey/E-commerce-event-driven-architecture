import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';

export type FeedbackTone = 'success' | 'error' | 'info';

@Component({
  selector: 'app-feedback-alert',
  standalone: true,
  imports: [CommonModule],
  template: `
    <p class="mt-4 rounded-xl border px-3 py-2 text-sm font-semibold" [ngClass]="classesByTone[tone]">
      {{ message }}
    </p>
  `,
})
export class FeedbackAlertComponent {
  @Input({ required: true }) message = '';
  @Input() tone: FeedbackTone = 'info';

  readonly classesByTone: Record<FeedbackTone, string> = {
    success: 'border-success/60 bg-success/15 text-[#d6efc7]',
    error: 'border-danger/60 bg-danger/15 text-[#ffc6be]',
    info: 'border-brand-700/60 bg-brand-900/35 text-brand-100',
  };
}
