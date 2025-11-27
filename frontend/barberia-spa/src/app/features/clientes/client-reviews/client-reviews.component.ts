import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { ClientNotificationResponse, ClientReview, LoyaltyActionResponse } from '../../../models';

@Component({
  selector: 'app-client-reviews',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './client-reviews.component.html'
})
export class ClientReviewsComponent {
  @Input() reviews: ClientReview[] = [];
  @Input() loyaltyActions: LoyaltyActionResponse[] = [];
  @Input() notifications: ClientNotificationResponse[] = [];
}
