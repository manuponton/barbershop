import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { ClientNotificationResponse, ClientSegment } from '../../../models';

@Component({
  selector: 'app-client-campaigns',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './client-campaigns.component.html'
})
export class ClientCampaignsComponent {
  @Input() segments: ClientSegment[] = [];
  @Input() notifications: ClientNotificationResponse[] = [];
}
