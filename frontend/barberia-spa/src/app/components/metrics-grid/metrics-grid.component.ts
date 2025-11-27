import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';

interface SummaryMetrics {
  barbers: number;
  clients: number;
  appointments: number;
}

@Component({
  selector: 'app-metrics-grid',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './metrics-grid.component.html'
})
export class MetricsGridComponent {
  @Input() summary: SummaryMetrics = { barbers: 0, clients: 0, appointments: 0 };
}
