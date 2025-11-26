# Arquitectura y diseño del SaaS para barberías

Este documento consolida la propuesta de arquitectura, los módulos de dominio, los planes de suscripción y el roadmap de evolución para el SaaS de barberías. El enfoque aplica DDD, arquitectura hexagonal, CQRS, SOLID, Clean Code y TDD.

## 1. Objetivo y alcance
El sistema permite a las barberías gestionar citas, barberos, clientes, inventario, caja y reportes, con funcionalidades diferenciadas por plan (Básico, Pro, Premium). El backend parte de un monolito modular en Java y el frontend es una SPA/PWA en Angular.

## 2. Arquitectura general
### 2.1 Visión de alto nivel
El backend sigue arquitectura hexagonal: cada contexto de dominio expone puertos para adaptadores de entrada (REST, eventos) y salida (persistencia, pagos, mensajería). El frontend consume las APIs vía HTTP/JSON y websockets. CQRS separa comandos y consultas, habilitando modelos de lectura optimizados.

### 2.2 Diagrama conceptual
```mermaid
flowchart LR
    subgraph Frontend [Frontend (Angular SPA / PWA)]
        UI["UI Angular (Páginas y componentes)"]
    end
    subgraph API [Backend – Capa de aplicación]
        CommandBus["Bus de Comandos"]
        QueryBus["Bus de Consultas"]
    end
    UI -- "REST Comandos" --> CommandBus
    UI -- "REST Consultas" --> QueryBus
    subgraph Dominio [Backend – Contextos del dominio]
        direction TB
        subgraph CitasContext [Citas]
            CitasApp["AppService Citas"]
            CitasDomain["Dominio Citas (Agregado Cita)"]
            CitasInfra["Adaptadores e Infraestructura"]
        end
        subgraph BarberosContext [Barberos]
            BarberosApp
            BarberosDomain
            BarberosInfra
        end
        subgraph ClientesContext [Clientes]
            ClientesApp
            ClientesDomain
            ClientesInfra
        end
        subgraph InventarioContext [Inventario]
            InventarioApp
            InventarioDomain
            InventarioInfra
        end
        subgraph CajaContext [Caja]
            CajaApp
            CajaDomain
            CajaInfra
        end
        subgraph ReportesContext [Reportes]
            ReportesApp
            ReportesDomain
            ReportesInfra
        end
    end
    CommandBus --> CitasApp & BarberosApp & ClientesApp & InventarioApp & CajaApp & ReportesApp
    QueryBus --> CitasApp & BarberosApp & ClientesApp & InventarioApp & CajaApp & ReportesApp
    CitasApp --> CitasDomain
    CitasDomain -- "Eventos de dominio" --> CitasInfra
    BarberosApp --> BarberosDomain
    BarberosDomain --> BarberosInfra
    ClientesApp --> ClientesDomain
    ClientesDomain --> ClientesInfra
    InventarioApp --> InventarioDomain
    InventarioDomain --> InventarioInfra
    CajaApp --> CajaDomain
    CajaDomain --> CajaInfra
    ReportesApp --> ReportesDomain
    ReportesDomain --> ReportesInfra
    subgraph External [Servicios Externos]
        DB[(Bases de datos SQL/NoSQL)]
        Payment[(Pasarela de Pago)]
        Messaging[(Bus de Mensajería)]
        Notifications[(Email/SMS/Push)]
    end
    CitasInfra -- "Persistencia" --> DB
    BarberosInfra -- "Persistencia" --> DB
    ClientesInfra -- "Persistencia" --> DB
    InventarioInfra -- "Persistencia" --> DB
    CajaInfra -- "Transacciones" --> Payment
    CajaInfra -- "Mensajes/Events" --> Messaging
    ReportesInfra -- "Consultas analíticas" --> DB
    Messaging -- "Pub/Sub" --> AllContexts
    AllContexts --> Messaging
```

## 3. Módulos principales y planes de suscripción
Los contextos delimitados aplican lenguaje ubicuo y agregados propios.

- **Citas**: agenda, solapamientos, walk-ins/espera (Pro+), recordatorios y canales de reserva. 
- **Barberos**: perfiles, horarios/vacaciones, comisiones y control de acceso.
- **Clientes**: registro, historial, marketing/fidelidad y reseñas (Premium).
- **Inventario** (Pro+): catálogo, stock, compras y ventas minoristas.
- **Caja** (Pro+): facturación POS, conciliación, pasarela y nómina (Premium).
- **Reportes** (Pro+): operativos, financieros y dashboards avanzados (Premium).
- **Servicios transversales**: autenticación/autorización, notificaciones, mensajería y multi-sucursal (Premium).

### Requerimientos funcionales adicionales
Los siguientes temas deben incorporarse de manera explícita en los contextos y planes existentes:

- **Módulo de agendamiento**: gestión fina de slots, reglas de disponibilidad, bloqueos manuales y optimización de turnos por barbero.
- **Trazabilidad de servicios**: bitácora de estados de cada cita (creada, confirmada, en servicio, completada, cancelada) con auditoría de cambios.
- **Manejo de data por barbero**: analítica por profesional (rendimiento, ticket promedio, recurrencia de clientes, tiempos muertos) y comparativos entre sucursales.
- **Productos**: catálogo de retail y consumo interno ligado a inventario y caja; soporte para combos/paquetes junto con servicios.
- **Temas de los servicios**: taxonomía de servicios (cortes clásicos, grooming, coloración, tratamientos) para segmentar catálogos y búsquedas.
- **Ciclo de vida del cliente**: estados y eventos clave (lead, primer servicio, recurrente, inactivo) asociados a campañas y recordatorios.
- **Servicios complementarios**: cross-sell/up-sell configurables (bebidas, tratamientos express, membresías) sugeridos en agenda y POS.
- **Reportes de marketing**: tableros y exportes para identificar estrategias (segmentos rentables, canales de adquisición, cohortes y conversión por campaña).

### Tabla de planes
| Funcionalidad clave | Básico | Pro | Premium |
| --- | --- | --- | --- |
| Gestión de citas, agenda por barbero, barberos y clientes | ✔︎ | ✔︎ | ✔︎ |
| Notificaciones | ✔︎ | ✔︎ | ✔︎ |
| Lista de espera / auto-check-in | ✖︎ | ✔︎ | ✔︎ |
| Inventario, caja, reportes básicos | ✖︎ | ✔︎ | ✔︎ |
| Dashboards, marketing, fidelidad, multi-sucursal, e-commerce, branding | ✖︎ | ✖︎ | ✔︎ |

## 4. Diseño de dominio
- **Entidades** (Barbero, Cliente, Producto, Cita, Factura) con identidad; **Value Objects** inmutables (Horario, Dirección, Precio, EstadoCita).
- **Agregados**: Cita, Barbero, Cliente, Producto, Factura. Operan con consistencia interna y publican eventos de dominio.
- **Servicios de dominio**: PlanificadorDeCitas (disponibilidad y penalizaciones), CalculadorComisiones, GestorInventario, ProcesadorPagos, GeneradorInformes.

## 5. CQRS y eventos de dominio
- Comandos inmutables con handlers que coordinan repositorios y servicios; generan eventos (por ejemplo, `CitaCreada`, `PagoRegistrado`).
- Queries usan proyecciones/read models optimizados. Se favorece consistencia eventual entre agregados y contextos.
- Integración con bus de mensajería y patrón outbox para entrega fiable.

## 6. API REST y comandos/consultas
- API versionada `/api/v1` con recursos por contexto (citas, barberos, clientes, inventario, caja, reportes).
- Ejemplos: `POST /api/v1/citas`, `PUT /api/v1/citas/{id}/cancelar`, `POST /api/v1/facturas`, `GET /api/v1/reportes/ventas`.
- Comandos principales: `CrearCitaCommand`, `CancelarCitaCommand`, `RegistrarClienteCommand`, `CrearProductoCommand`, `RegistrarPagoCommand`.
- Queries principales: `ListarCitasQuery`, `ObtenerClienteQuery`, `GenerarReporteVentasQuery`.

## 7. Estrategia de TDD
- Ciclo Rojo-Verde-Refactor en cada caso de uso.
- Pruebas de dominio (entidades/VO/servicios), integración de aplicación (handlers/repositorios), contrato de API y E2E.
- Frontend: pruebas de componentes y servicios, integración de módulos y E2E con Cypress.

## 8. Roadmap de evolución
1. **MVP (0–3 meses)**: contextos Citas, Barberos y Clientes; autenticación básica; pipeline CI/CD inicial.
2. **Fase Pro (4–6 meses)**: Inventario, Caja, Reportes, pasarela de pagos, listas de espera y recordatorios.
3. **Fase Premium (7–12 meses)**: marketing/fidelidad, multi-sucursal, e-commerce, dashboards y personalización de marca.
4. **Escalabilidad (>12 meses)**: extraer microservicios de contextos con más carga, bases de datos separadas de lectura/escritura, event sourcing selectivo y observabilidad.

## 9. Buenas prácticas
- Aplicar principios SOLID, Clean Code, inyección de dependencias, API versionada y seguridad desde el diseño.
- Arquitectura hexagonal: dominio independiente de infraestructura; adaptadores de entrada/salida intercambiables.
- Automatizar CI/CD con pruebas, análisis estático, empaquetado y despliegue.
