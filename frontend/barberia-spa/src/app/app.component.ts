import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit, computed, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { forkJoin } from 'rxjs';
import {
  AppointmentPayload,
  AppointmentResponse,
  BarberResponse,
  ClientPayload,
  ClientResponse,
  EndpointDescriptor,
  FeatureCard
} from './models';
import { HeroSectionComponent } from './components/hero-section/hero-section.component';
import { MetricsGridComponent } from './components/metrics-grid/metrics-grid.component';
import { ClientFormComponent } from './components/client-form/client-form.component';
import { AppointmentFormComponent } from './components/appointment-form/appointment-form.component';
import { CatalogPanelComponent } from './components/catalog-panel/catalog-panel.component';
import { EndpointsListComponent } from './components/endpoints-list/endpoints-list.component';
import { FeatureMapComponent } from './components/feature-map/feature-map.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    CommonModule,
    RouterOutlet,
    HeroSectionComponent,
    MetricsGridComponent,
    ClientFormComponent,
    AppointmentFormComponent,
    CatalogPanelComponent,
    EndpointsListComponent,
    FeatureMapComponent
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent implements OnInit {
  readonly title = 'Barbería SaaS';
  readonly apiBase = '/api';

  readonly featureMatrix: FeatureCard[] = [
    {
      context: 'Citas',
      description: 'Agenda nuevas citas y consulta las próximas visitas de clientes.',
      status: 'online',
      actions: [
        'Crear citas con duración validada',
        'Listar próximas citas en vivo',
        'Refrescar manualmente el listado'
      ]
    },
    {
      context: 'Barberos',
      description: 'Consulta el catálogo sembrado de barberos y sus servicios.',
      status: 'online',
      actions: ['Catálogo base disponible', 'Servicios ofrecidos por barbero', 'Compatibilidad con formulario de citas']
    },
    {
      context: 'Clientes',
      description: 'Registra y consulta clientes con validaciones en el frontend.',
      status: 'online',
      actions: ['Validación de email y nombre', 'Reset de formulario con mensajes', 'Selección directa en agenda de citas']
    },
    {
      context: 'Inventario',
      description: 'Catálogo de productos y control de stock planificado para iteración siguiente.',
      status: 'en-progreso',
      actions: ['Modelado de productos y variantes', 'Alertas de stock bajo', 'Integración con caja y servicios']
    },
    {
      context: 'Caja',
      description: 'Cierre de caja y seguimiento de ventas programado para la versión Pro.',
      status: 'en-progreso',
      actions: ['Registro de ventas por servicio', 'Conciliación diaria', 'Métricas de tickets por barbero']
    },
    {
      context: 'Reportes',
      description: 'Reportes de marketing y retención para clientes frecuentes en el roadmap.',
      status: 'en-progreso',
      actions: ['Panel de retención y recurrencia', 'Analítica por barbero', 'Exportación de métricas']
    }
  ];

  readonly endpoints: EndpointDescriptor[] = [
    { path: '/api/citas', description: 'Crear y consultar citas (POST, GET)' },
    { path: '/api/barberos', description: 'Catálogo base de barberos' },
    { path: '/api/clientes', description: 'Registro y listado de clientes' }
  ];

  readonly barbers = signal<BarberResponse[]>([]);
  readonly clients = signal<ClientResponse[]>([]);
  readonly appointments = signal<AppointmentResponse[]>([]);

  readonly hasBarbers = computed(() => this.barbers().length > 0);
  readonly hasClients = computed(() => this.clients().length > 0);
  readonly hasAppointments = computed(() => this.appointments().length > 0);
  readonly summary = computed(() => ({
    barbers: this.barbers().length,
    clients: this.clients().length,
    appointments: this.appointments().length
  }));
  readonly sortedAppointments = computed(() =>
    [...this.appointments()].sort((a, b) => new Date(a.startAt).getTime() - new Date(b.startAt).getTime())
  );

  requestError = '';
  requestSuccess = '';
  loadingCatalogs = false;
  loadingAppointments = false;
  submittingClient = false;
  submittingAppointment = false;
  clientResetKey = 0;
  appointmentResetKey = 0;

  constructor(private readonly http: HttpClient) {}

  ngOnInit(): void {
    this.loadCatalogs();
    this.loadAppointments();
  }

  loadCatalogs(): void {
    this.loadingCatalogs = true;

    forkJoin({
      barbers: this.http.get<BarberResponse[]>(`${this.apiBase}/barberos`),
      clients: this.http.get<ClientResponse[]>(`${this.apiBase}/clientes`)
    }).subscribe({
      next: ({ barbers, clients }) => {
        this.barbers.set(barbers);
        this.clients.set(clients);
        this.loadingCatalogs = false;
      },
      error: (err) => {
        this.loadingCatalogs = false;
        this.handleError('No se pudieron cargar los catálogos', err);
      }
    });
  }

  loadAppointments(): void {
    this.loadingAppointments = true;

    this.http.get<AppointmentResponse[]>(`${this.apiBase}/citas`).subscribe({
      next: (citas) => this.appointments.set(citas),
      error: (err) => this.handleError('No se pudieron cargar las citas', err),
      complete: () => (this.loadingAppointments = false)
    });
  }

  registerClient(payload: ClientPayload): void {
    this.requestError = '';
    this.requestSuccess = '';

    if (this.submittingClient) {
      return;
    }

    this.submittingClient = true;

    this.http.post<ClientResponse>(`${this.apiBase}/clientes`, payload).subscribe({
      next: (client) => {
        this.clients.set([client, ...this.clients()]);
        this.requestSuccess = `Cliente ${client.name} registrado.`;
        this.submittingClient = false;
        this.clientResetKey++;
      },
      error: (err) => {
        this.submittingClient = false;
        this.handleError('No se pudo registrar el cliente', err);
      }
    });
  }

  createAppointment(payload: AppointmentPayload): void {
    this.requestError = '';
    this.requestSuccess = '';

    if (this.submittingAppointment) {
      return;
    }

    this.submittingAppointment = true;

    const body = {
      ...payload,
      startAt: payload.startAt,
      durationMinutes: Number(payload.duration)
    };

    this.http.post<AppointmentResponse>(`${this.apiBase}/citas`, body).subscribe({
      next: (appointment) => {
        this.appointments.set([appointment, ...this.appointments()]);
        this.requestSuccess = 'Cita creada correctamente.';
        this.submittingAppointment = false;
        this.appointmentResetKey++;
      },
      error: (err) => {
        this.submittingAppointment = false;
        this.handleError('No se pudo crear la cita', err);
      }
    });
  }

  private handleError(message: string, err: unknown): void {
    console.error(message, err);
    this.requestError = message;
  }
}
