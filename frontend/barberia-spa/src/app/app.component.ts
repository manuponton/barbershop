import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit, computed, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterOutlet } from '@angular/router';

interface BarberResponse {
  name: string;
  specialty: string;
}

interface ClientResponse {
  name: string;
  email: string;
  birthday: string;
}

interface AppointmentResponse {
  id: string;
  clientName: string;
  barberName: string;
  service: string;
  startAt: string;
  duration: number;
}

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, ReactiveFormsModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent implements OnInit {
  readonly title = 'Barbería SaaS';
  readonly apiBase = '/api';

  readonly contexts = ['Citas', 'Barberos', 'Clientes', 'Inventario', 'Caja', 'Reportes'];

  readonly endpoints = [
    { path: '/api/citas', description: 'Crear y consultar citas (POST, GET)' },
    { path: '/api/barberos', description: 'Catálogo base de barberos' },
    { path: '/api/clientes', description: 'Registro y listado de clientes' }
  ];

  readonly clientForm = this.fb.group({
    name: ['', [Validators.required, Validators.minLength(3)]],
    email: ['', [Validators.required, Validators.email]],
    birthday: ['', Validators.required]
  });

  readonly appointmentForm = this.fb.group({
    clientName: ['', Validators.required],
    barberName: ['', Validators.required],
    service: ['Corte + estilo', Validators.required],
    startAt: [this.defaultStartAt(), Validators.required],
    duration: [30, [Validators.required, Validators.min(10)]]
  });

  readonly barbers = signal<BarberResponse[]>([]);
  readonly clients = signal<ClientResponse[]>([]);
  readonly appointments = signal<AppointmentResponse[]>([]);

  readonly hasBarbers = computed(() => this.barbers().length > 0);
  readonly hasClients = computed(() => this.clients().length > 0);
  readonly hasAppointments = computed(() => this.appointments().length > 0);

  requestError = '';
  requestSuccess = '';

  constructor(private readonly http: HttpClient, private readonly fb: FormBuilder) {}

  ngOnInit(): void {
    this.loadCatalogs();
    this.loadAppointments();
  }

  loadCatalogs(): void {
    this.http.get<BarberResponse[]>(`${this.apiBase}/barberos`).subscribe({
      next: (barbers) => this.barbers.set(barbers),
      error: (err) => this.handleError('No se pudieron cargar los barberos', err)
    });

    this.http.get<ClientResponse[]>(`${this.apiBase}/clientes`).subscribe({
      next: (clients) => this.clients.set(clients),
      error: (err) => this.handleError('No se pudieron cargar los clientes', err)
    });
  }

  loadAppointments(): void {
    this.http.get<AppointmentResponse[]>(`${this.apiBase}/citas`).subscribe({
      next: (citas) => this.appointments.set(citas),
      error: (err) => this.handleError('No se pudieron cargar las citas', err)
    });
  }

  registerClient(): void {
    this.requestError = '';
    this.requestSuccess = '';

    if (this.clientForm.invalid) {
      this.clientForm.markAllAsTouched();
      return;
    }

    const payload = this.clientForm.getRawValue();

    this.http.post<ClientResponse>(`${this.apiBase}/clientes`, payload).subscribe({
      next: (client) => {
        this.clients.set([client, ...this.clients()]);
        this.requestSuccess = `Cliente ${client.name} registrado.`;
        this.clientForm.reset();
      },
      error: (err) => this.handleError('No se pudo registrar el cliente', err)
    });
  }

  createAppointment(): void {
    this.requestError = '';
    this.requestSuccess = '';

    if (this.appointmentForm.invalid) {
      this.appointmentForm.markAllAsTouched();
      return;
    }

    const raw = this.appointmentForm.getRawValue();
    const payload = {
      ...raw,
      startAt: new Date(raw.startAt as string).toISOString(),
      duration: Number(raw.duration)
    };

    this.http.post<AppointmentResponse>(`${this.apiBase}/citas`, payload).subscribe({
      next: (appointment) => {
        this.appointments.set([appointment, ...this.appointments()]);
        this.requestSuccess = 'Cita creada correctamente.';
        this.appointmentForm.reset({
          service: 'Corte + estilo',
          duration: 30,
          startAt: this.defaultStartAt(),
          clientName: '',
          barberName: ''
        });
      },
      error: (err) => this.handleError('No se pudo crear la cita', err)
    });
  }

  private handleError(message: string, err: unknown): void {
    console.error(message, err);
    this.requestError = message;
  }

  private defaultStartAt(): string {
    const date = new Date();
    date.setMinutes(date.getMinutes() + 30);
    return date.toISOString().slice(0, 16);
  }
}
