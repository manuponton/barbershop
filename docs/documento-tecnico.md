# Documento técnico del proyecto BarberShop SaaS

Este documento resume las decisiones técnicas, la arquitectura y la funcionalidad implementada en el monorepo. Se complementa con `docs/arquitectura.md`, que detalla el modelo de dominio y el roadmap.

## Panorama general
- **Dominio:** SaaS para barberías con planes Básico, Pro y Premium.
- **Stack:** Backend Java 17 con Spring Boot 3 (WebFlux) y frontend Angular (SPA/PWA) con señales y formularios reactivos.
- **Principios:** DDD, arquitectura hexagonal, CQRS, SOLID, Clean Code y TDD.
- **Repositorios lógicos:** `backend/` y `frontend/` comparten un roadmap común descrito en `docs/arquitectura.md`.

## Arquitectura lógica
- **Hexagonal:** Contextos de dominio separados (citas, barberos, clientes). Cada módulo expone puertos de entrada (REST) y se apoya en servicios de aplicación para orquestar casos de uso.
- **CQRS light:** Controladores distinguen comandos (creación) y consultas (listados), aunque comparten el mismo servicio en esta iteración. Las consultas ordenan y filtran los read models en memoria.
- **DTOs y validación:** Los adaptadores REST usan records para requests/responses y validación con Jakarta Validation antes de invocar la capa de aplicación.
- **In-memory first:** Los servicios de dominio mantienen el estado en mapas concurrentes o listas; la persistencia real queda pendiente para futuras iteraciones.

## Backend (Spring Boot WebFlux)
- **Arranque:** `BarberiaSaasApplication` inicializa los beans y el stack reactivo.
- **Citas:** `AppointmentService` crea y lista citas futuras desde una colección concurrente, generando IDs UUID y ordenando por fecha de inicio. `AppointmentController` expone `POST /api/citas` y `GET /api/citas` con validación de entrada y conversión a DTO.
- **Barberos:** `BarberCatalog` provee un catálogo semilla consultado vía `GET /api/barberos` a través de `BarberController`.
- **Clientes:** `ClientRegistry` registra clientes validados y los expone mediante `ClientController` en `/api/clientes`.
- **Dominio:** Entidades inmutables (`Appointment`, `Barber`, `Client`) encapsulan identidad y datos básicos; los value objects adicionales se incorporarán al crecer el dominio.
- **Configuración mínima:** `build.gradle` define dependencias de WebFlux, validación y prueba; `settings.gradle` fija el root project.

### Endpoints implementados
- `POST /api/citas`: crea una cita con `clientName`, `barberName`, `service`, `startAt`, `durationMinutes`.
- `GET /api/citas`: lista citas futuras (desde `from` opcional o el instante actual) ordenadas por fecha.
- `GET /api/barberos`: devuelve barberos sembrados con servicios ofertados.
- `POST /api/clientes`: registra clientes con nombre, email y fecha de nacimiento.
- `GET /api/clientes`: lista clientes registrados.

### Consideraciones de calidad
- **Validaciones:** Campos obligatorios y formatos (ej. email) en payloads de entrada.
- **Concurrencia:** Uso de `ConcurrentHashMap` para evitar carreras en escritura de citas.
- **Extensibilidad:** Separación de DTOs, controladores y servicios facilita sustituir persistencia in-memory por repositorios reactivos y añadir eventos/outbox.

## Frontend (Angular SPA)
- **Tecnologías:** Angular standalone components, `HttpClient`, formularios reactivos, señales (`signal`, `computed`).
- **Componente principal:** `AppComponent` carga catálogos iniciales (`/api/barberos`, `/api/clientes`) y citas (`/api/citas`) al iniciar.
- **Formularios:**
  - **Clientes:** validación de nombre (mín. 3), email y fecha; al crear, actualiza la lista local y muestra mensaje de éxito.
  - **Citas:** validación de campos obligatorios, duración mínima y fecha por defecto 30 minutos en el futuro; al crear, reinicia el formulario con valores por defecto y refresca la tabla local.
- **Estado y UX:** señales para listas y flags derivados (`hasBarbers`, `hasClients`, `hasAppointments`); manejo básico de errores con mensajes en UI.
- **Integración:** `apiBase=/api` y `proxy.conf.json` para enrutar hacia el backend en `localhost:8080` durante desarrollo.

## Flujo de trabajo recomendado
1. **Backend:** generar wrapper (`gradle wrapper --gradle-version 8.14.3`) y ejecutar `./gradlew bootRun` desde `backend/`.
2. **Frontend:** `npm install` y `npm start` en `frontend/barberia-spa`; la app se sirve en `http://localhost:4200`.
3. **Validación rápida:**
   - Crear clientes y citas desde la UI.
   - Verificar listados dinámicos de clientes, barberos seed y citas ordenadas.

## Próximos pasos sugeridos
- Sustituir almacenamiento en memoria por repositorios reactivos (por contexto) y eventos de dominio con patrón outbox.
- Formalizar CQRS con buses de comando/consulta y proyecciones para reportes.
- Añadir autenticación/autorización, notificaciones y pruebas automatizadas (unitarias, integración y E2E) en CI/CD.
- Estructurar el frontend con módulos por contexto y gestión de estado (NgRx o signals compartidas) para escalar features Pro/Premium.
