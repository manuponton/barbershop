import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit, computed, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { forkJoin } from 'rxjs';
import {
  AppointmentPayload,
  AppointmentResponse,
  BarberResponse,
  CashSessionResponse,
  ClientPayload,
  ClientResponse,
  EndpointDescriptor,
  FeatureCard,
  MovementPayload,
  PaymentPayload,
  ProductPayload,
  ProductResponse,
  SalesProjection,
  SalesReport,
  StockSnapshot
} from './models';
import { HeroSectionComponent } from './components/hero-section/hero-section.component';
import { MetricsGridComponent } from './components/metrics-grid/metrics-grid.component';
import { ClientFormComponent } from './components/client-form/client-form.component';
import { AppointmentFormComponent } from './components/appointment-form/appointment-form.component';
import { CatalogPanelComponent } from './components/catalog-panel/catalog-panel.component';
import { EndpointsListComponent } from './components/endpoints-list/endpoints-list.component';
import { FeatureMapComponent } from './components/feature-map/feature-map.component';
import { InventoryViewComponent } from './features/inventory-view/inventory-view.component';
import { SalesViewComponent } from './features/sales-view/sales-view.component';

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
    FeatureMapComponent,
    InventoryViewComponent,
    SalesViewComponent
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent implements OnInit {
  readonly title = 'Barbería SaaS';
  readonly apiBase = '/api';
  readonly apiV1 = '/api/v1';

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
      description: 'Catálogo de productos y control de stock para el plan Pro.',
      status: 'online',
      actions: ['Crear productos retail e internos', 'Registrar compras y ventas', 'Reportes básicos de stock y consumo']
    },
    {
      context: 'Caja',
      description: 'Facturación POS con integración a pasarela y arqueo diario.',
      status: 'online',
      actions: ['Apertura y cierre de caja', 'Pagos POS con pasarela simulada', 'Reporte de ventas aprobadas/rechazadas']
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
    { path: '/api/clientes', description: 'Registro y listado de clientes' },
    { path: '/api/v1/inventario', description: 'Productos, compras/ventas y reportes de stock' },
    { path: '/api/v1/caja', description: 'Apertura/cierre de caja, pagos POS y reportes de ventas' }
  ];

  readonly barbers = signal<BarberResponse[]>([]);
  readonly clients = signal<ClientResponse[]>([]);
  readonly appointments = signal<AppointmentResponse[]>([]);
  readonly products = signal<ProductResponse[]>([]);
  readonly stockReport = signal<StockSnapshot[]>([]);
  readonly salesProjection = signal<SalesProjection[]>([]);
  readonly cashSession = signal<CashSessionResponse | null>(null);
  readonly salesReport = signal<SalesReport | null>(null);

  readonly hasBarbers = computed(() => this.barbers().length > 0);
  readonly hasClients = computed(() => this.clients().length > 0);
  readonly hasAppointments = computed(() => this.appointments().length > 0);
  readonly hasProducts = computed(() => this.products().length > 0);
  readonly summary = computed(() => ({
    barbers: this.barbers().length,
    clients: this.clients().length,
    appointments: this.appointments().length,
    products: this.products().length
  }));
  readonly sortedAppointments = computed(() =>
    [...this.appointments()].sort((a, b) => new Date(a.startAt).getTime() - new Date(b.startAt).getTime())
  );

  requestError = '';
  requestSuccess = '';
  loadingCatalogs = false;
  loadingAppointments = false;
  loadingInventory = false;
  loadingPayments = false;
  submittingClient = false;
  submittingAppointment = false;
  clientResetKey = 0;
  appointmentResetKey = 0;

  constructor(private readonly http: HttpClient) {}

  ngOnInit(): void {
    this.loadCatalogs();
    this.loadAppointments();
    this.loadInventory();
    this.loadCashData();
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

  loadInventory(): void {
    this.loadingInventory = true;
    forkJoin({
      products: this.http.get<ProductResponse[]>(`${this.apiV1}/inventario/productos`),
      stock: this.http.get<StockSnapshot[]>(`${this.apiV1}/inventario/reportes/stock`),
      sales: this.http.get<SalesProjection[]>(`${this.apiV1}/inventario/reportes/ventas`)
    }).subscribe({
      next: ({ products, stock, sales }) => {
        this.products.set(products);
        this.stockReport.set(stock);
        this.salesProjection.set(sales);
        this.loadingInventory = false;
      },
      error: (err) => {
        this.loadingInventory = false;
        this.handleError('No se pudo cargar inventario', err);
      }
    });
  }

  createProduct(payload: ProductPayload): void {
    this.http.post<ProductResponse>(`${this.apiV1}/inventario/productos`, payload).subscribe({
      next: (product) => {
        this.products.set([product, ...this.products()]);
        this.loadInventory();
        this.requestSuccess = `Producto ${product.name} creado.`;
      },
      error: (err) => this.handleError('No se pudo crear el producto', err)
    });
  }

  registerPurchase(payload: MovementPayload): void {
    this.http.post(`${this.apiV1}/inventario/movimientos/compra`, payload).subscribe({
      next: () => this.loadInventory(),
      error: (err) => this.handleError('No se pudo registrar la compra', err)
    });
  }

  registerSale(payload: MovementPayload): void {
    this.http.post(`${this.apiV1}/inventario/movimientos/venta`, payload).subscribe({
      next: () => this.loadInventory(),
      error: (err) => this.handleError('No se pudo registrar la venta', err)
    });
  }

  loadCashData(): void {
    forkJoin({
      session: this.http.get<CashSessionResponse | null>(`${this.apiV1}/caja/estado`),
      report: this.http.get<SalesReport>(`${this.apiV1}/caja/reportes/ventas`)
    }).subscribe({
      next: ({ session, report }) => {
        this.cashSession.set(session ? { ...session, open: session.closedAt === null } : null);
        this.salesReport.set(report);
      },
      error: (err) => this.handleError('No se pudo cargar el estado de caja', err)
    });
  }

  openCashRegister(amount: number): void {
    this.loadingPayments = true;
    this.http.post<CashSessionResponse>(`${this.apiV1}/caja/aperturas`, { openingAmount: amount }).subscribe({
      next: (session) => {
        this.cashSession.set({ ...session, open: true });
        this.loadingPayments = false;
      },
      error: (err) => {
        this.loadingPayments = false;
        this.handleError('No se pudo abrir la caja', err);
      }
    });
  }

  closeCashRegister(amount: number): void {
    this.loadingPayments = true;
    this.http.post<CashSessionResponse>(`${this.apiV1}/caja/cierres`, { closingAmount: amount }).subscribe({
      next: (session) => {
        this.cashSession.set({ ...session, open: false });
        this.loadingPayments = false;
      },
      error: (err) => {
        this.loadingPayments = false;
        this.handleError('No se pudo cerrar la caja', err);
      }
    });
  }

  registerPayment(payload: PaymentPayload): void {
    this.loadingPayments = true;
    this.http.post(`${this.apiV1}/caja/pagos`, payload).subscribe({
      next: () => {
        this.loadingPayments = false;
        this.loadCashData();
        this.requestSuccess = 'Pago registrado en POS.';
      },
      error: (err) => {
        this.loadingPayments = false;
        this.handleError('No se pudo registrar el pago', err);
      }
    });
  }

  private handleError(message: string, err: unknown): void {
    console.error(message, err);
    this.requestError = message;
  }
}
