# Mapa de funcionalidades

Este documento resume el estado actual del SaaS de barberías respecto a las funcionalidades esenciales y avanzadas, y prioriza las tareas por plan (MVP, Pro, Premium).

## Cobertura actual

### Esenciales cubiertas
- **Agendamiento básico**: creación y listado de citas con cliente, barbero y servicio textual, validando disponibilidad de sucursal. Basado en `/api/citas` y el `AppointmentService`. 
- **Gestión básica de clientes**: registro y consulta de clientes con reseñas y acciones de fidelización iniciales expuestas por `ClientController`.
- **Catálogo inicial de barberos**: consulta de barberos sembrados vía `BarberCatalog`.

### Esenciales parciales o faltantes
- **Citas enriquecidas**: faltan estados completos, asociación explícita a servicios definidos, manejo de no-show/cancelación y trazabilidad.
- **Calendario por barbero/semana**: no hay vistas/consultas diferenciadas ni bloqueo de solapes por barbero.
- **POS y ventas**: solo existe un esqueleto de caja/pagos; falta registrar ventas, totales y métodos de pago reales.
- **Inventario operativo**: se modela producto y movimientos, pero no hay reglas de stock mínimo/alertas ni salida automática por venta.
- **Reportes básicos**: no hay endpoints para ingresos, servicios más vendidos o métricas de citas.
- **Gestión de empleados**: faltan comisiones y horarios/turnos.
- **Marketing/Fidelización básica**: existen perfiles/segmentos, pero no se conectan a recordatorios ni descuentos aplicables.

### Avanzadas ausentes
Todas las funcionalidades avanzadas (multicanal, recomendaciones, pagos avanzados, tienda online, analítica avanzada, portal de empleado, marketing automatizado, programas de puntos/cupones) aún no están implementadas; solo existen conceptos de clientes/segmentos que se pueden extender.

## Roadmap por plan

### MVP (Esenciales obligatorias)
- Enriquecer citas con estados (BOOKED, CONFIRMED, COMPLETED, CANCELLED, NO_SHOW), referencia a servicio y cliente/barbero por id.
- Prevenir solapes de citas por barbero y ofrecer vistas día/semana con filtros.
- Catalogar servicios (nombre, duración, precio) y asociarlos a las citas.
- Inventario operativo: altas, ajustes, alertas de stock mínimo y bloqueo ante stock negativo.
- POS simple: registrar ventas (servicios/productos), métodos de pago, totales y generación de tickets básicos.
- Reportes básicos: ingresos por periodo, citas por barbero (completadas vs no-show) y servicios más vendidos.
- Comisiones simples y registro de horario por barbero.
- UX Angular: páginas de citas, clientes (con historial), servicios, inventario, ventas y dashboard de reportes.

### Pro (Avanzadas intermedias)
- Lista de espera y auto check-in en agenda.
- Depósitos por reserva y cargos por cancelación tardía en caja.
- Tienda online conectada a inventario con catálogo público.
- Portal del empleado con métricas de citas y comisiones.
- Segmentación para campañas y recordatorios programados.
- Proyecciones de inventario y reposición automática.

### Premium (Innovadoras/diferenciales)
- Recomendaciones personalizadas de servicios/productos por historial y preferencias.
- Analítica avanzada: retención, valor de cliente, comparativas por sucursal y KPIs profundos.
- Marketing automatizado multicanal, cupones, puntos y gift cards.
- Multi-sucursal y branding avanzado para apps/páginas de reserva.
- Integración de pagos móviles/pasarelas y políticas complejas de depósitos.

## Preparación para funcionalidades avanzadas
- **Recomendaciones**: extender el contexto de clientes con histórico de servicios y compras para alimentar un servicio de sugerencias.
- **Depósitos/cancelaciones**: ampliar el agregado de cita con campos de depósito y reglas de penalización, conectados a caja.
- **Fidelización**: definir objetos de valor para puntos, cupones y gift cards vinculados a ventas y segmentación.
- **E-commerce**: reutilizar inventario y servicios para un catálogo público con órdenes en línea y sincronización de stock.
