import { CommonModule } from '@angular/common';
import { Component, Input, computed, signal } from '@angular/core';
import { ClientResponse, ClientSegment } from '../../../models';

interface CohortSummary {
  name: string;
  count: number;
  highlight: string;
}

@Component({
  selector: 'app-client-cohorts',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './client-cohorts.component.html'
})
export class ClientCohortsComponent {
  private readonly cohortData = signal<CohortSummary[]>([]);

  @Input() set clients(value: ClientResponse[]) {
    const lifecycleCount = value.reduce<Record<string, number>>((acc, client) => {
      acc[client.lifecycleStatus] = (acc[client.lifecycleStatus] || 0) + 1;
      return acc;
    }, {});

    const summaries = Object.entries(lifecycleCount).map<CohortSummary>(([status, count]) => ({
      name: status,
      count,
      highlight: status === 'VIP' ? 'Listos para referidos' : status === 'EN_RIESGO' ? 'Necesitan reactivación' : 'En seguimiento'
    }));

    this.cohortData.set(summaries);
  }

  @Input() segments: ClientSegment[] = [];

  readonly hasSegments = computed(() => this.segments.length > 0);
  readonly cohorts = computed(() => this.cohortData());
}
