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

export interface ProductResponse {
  id: string;
  name: string;
  category: string;
  price: number;
  stock: number;
}

export interface StockSnapshot {
  productId: string;
  name: string;
  stock: number;
  unitPrice: number;
  category: string;
}

export interface SalesProjection {
  productName: string;
  quantitySold: number;
  totalAmount: number;
}

export interface PaymentResponse {
  id: string;
  description: string;
  amount: number;
  method: string;
  status: string;
  authorization: string;
  processedAt: string;
}

export interface CashSessionResponse {
  id: string;
  openedAt: string;
  closedAt: string | null;
  openingAmount: number;
  closingAmount: number | null;
  open: boolean;
}

export interface SalesReport {
  totalAmount: number;
  approvedCount: number;
  declinedCount: number;
  payments: PaymentResponse[];
}

export interface ProductPayload {
  name: string;
  category: string;
  price: number;
  stock: number;
}

export interface MovementPayload {
  productId: string;
  quantity: number;
  unitPrice: number;
  note?: string;
}

export interface PaymentPayload {
  description: string;
  amount: number;
  method: string;
}
