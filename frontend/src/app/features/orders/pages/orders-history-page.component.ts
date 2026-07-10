import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { EmptyStateCardComponent } from '../../../shared/ui/empty-state-card/empty-state-card.component';
import { OrderHistoryService } from '../../checkout/services/order-history.service';

@Component({
  selector: 'app-orders-history-page',
  standalone: true,
  imports: [CommonModule, RouterLink, EmptyStateCardComponent],
  templateUrl: './orders-history-page.component.html',
})
export class OrdersHistoryPageComponent {
  constructor(readonly orderHistory: OrderHistoryService) {}
}