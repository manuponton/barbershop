export interface BarberResponse {
  id: string;
  name: string;
  services: string[];
}

export interface ClientResponse {
  id: string;
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
}

export interface AppointmentPayload {
  clientName: string;
  barberName: string;
  service: string;
  startAt: string;
  duration: number;
}
