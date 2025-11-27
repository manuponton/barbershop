export interface BarberResponse {
  id: string;
  sucursalId: string;
  name: string;
  services: string[];
}

export interface ClientResponse {
  id: string;
  sucursalId: string;
  name: string;
  email: string;
  birthday: string;
}

export interface AppointmentResponse {
  id: string;
  clientName: string;
  barberName: string;
  service: string;
  startAt: string;
  durationMinutes: number;
  sucursalId: string;
}

export interface FeatureCard {
  context: string;
  description: string;
  status: 'online' | 'en-progreso';
  actions: string[];
}

export interface EndpointDescriptor {
  path: string;
  description: string;
}

export interface ClientPayload {
  name: string;
  email: string;
  birthday: string;
  sucursalId: string;
}

export interface AppointmentPayload {
  clientName: string;
  barberName: string;
  service: string;
  startAt: string;
  duration: number;
  sucursalId: string;
}

export interface BrandingConfig {
  primaryColor: string;
  secondaryColor: string;
  accentColor: string;
  logoText: string;
  tagline: string;
}

export interface AvailabilityRule {
  dia: string;
  abre: string;
  cierra: string;
  online: boolean;
}

export interface SucursalResponse {
  id: string;
  nombre: string;
  slug: string;
  direccion: string;
  ciudad: string;
  branding: BrandingConfig;
  disponibilidad: AvailabilityRule[];
}

export interface CatalogItem {
  id: string;
  nombre: string;
  descripcion: string;
  categoria: string;
  precio: number;
  duracionMinutos?: number;
}

export interface CatalogoResponse {
  sucursalId: string;
  items: CatalogItem[];
}
