import { CommonModule, DatePipe } from '@angular/common';
import { Component, Input } from '@angular/core';
import { AppointmentResponse } from '../../models';

@Component({
  selector: 'app-appointments-table',
  standalone: true,
  imports: [CommonModule, DatePipe],
  templateUrl: './appointments-table.component.html'
})
export class AppointmentsTableComponent {
  @Input() appointments: AppointmentResponse[] = [];
  @Input() loading = false;
}
