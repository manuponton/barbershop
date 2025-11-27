import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { AppointmentPayload, BarberResponse, ClientResponse } from '../../models';

@Component({
  selector: 'app-appointment-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './appointment-form.component.html'
})
export class AppointmentFormComponent implements OnChanges {
  @Input() clients: ClientResponse[] = [];
  @Input() barbers: BarberResponse[] = [];
  @Input() submitting = false;
  @Input() resetKey = 0;
  @Output() submitAppointment = new EventEmitter<AppointmentPayload>();

  readonly appointmentForm = this.fb.group({
    clientName: ['', Validators.required],
    barberName: ['', Validators.required],
    service: ['Corte + estilo', Validators.required],
    startAt: [this.defaultStartAt(), Validators.required],
    duration: [30, [Validators.required, Validators.min(10)]]
  });

  constructor(private readonly fb: FormBuilder) {}

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['resetKey'] && !changes['resetKey'].firstChange) {
      this.resetForm();
    }
  }

  get isDisabled(): boolean {
    return !this.clients.length || !this.barbers.length || this.submitting;
  }

  onSubmit(): void {
    if (this.appointmentForm.invalid) {
      this.appointmentForm.markAllAsTouched();
      return;
    }

    const raw = this.appointmentForm.getRawValue();
    this.submitAppointment.emit({
      ...(raw as AppointmentPayload),
      duration: Number(raw.duration)
    });
  }

  private resetForm(): void {
    this.appointmentForm.reset({
      service: 'Corte + estilo',
      duration: 30,
      startAt: this.defaultStartAt(),
      clientName: '',
      barberName: ''
    });
  }

  private defaultStartAt(): string {
    const date = new Date();
    date.setMinutes(date.getMinutes() + 30);
    return date.toISOString().slice(0, 16);
  }
}
