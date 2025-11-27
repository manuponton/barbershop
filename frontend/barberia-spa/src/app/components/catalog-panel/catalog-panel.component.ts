import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { AppointmentResponse, BarberResponse, ClientResponse } from '../../models';
import { AppointmentsTableComponent } from '../appointments-table/appointments-table.component';

@Component({
  selector: 'app-catalog-panel',
  standalone: true,
  imports: [CommonModule, AppointmentsTableComponent],
  templateUrl: './catalog-panel.component.html'
})
export class CatalogPanelComponent {
  @Input() barbers: BarberResponse[] = [];
  @Input() clients: ClientResponse[] = [];
  @Input() appointments: AppointmentResponse[] = [];
  @Input() loadingCatalogs = false;
  @Input() loadingAppointments = false;

  @Output() refreshCatalogs = new EventEmitter<void>();
  @Output() refreshAppointments = new EventEmitter<void>();
}
