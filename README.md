# BarberShop SaaS

Diseño y base funcional para un SaaS de barberías con planes de suscripción (Básico, Pro y Premium). El sistema aplica DDD, arquitectura hexagonal, CQRS, SOLID, Clean Code y TDD.

## Estructura del repositorio
- `docs/arquitectura.md`: detalles completos de arquitectura, módulos, planes y roadmap.
- `backend/`: backend Java Spring Boot WebFlux con contextos de ejemplo (citas, barberos, clientes).
- `frontend/barberia-spa`: SPA Angular generada con Angular CLI, conectada al backend para registrar clientes y agendar citas.

## Cómo ejecutar
### Backend (Java)
1. Requiere JDK 17+. Para evitar binaries en el repo, genera localmente el wrapper:
   ```bash
   cd backend
   gradle wrapper --gradle-version 8.14.3
   ```
   (si ya tienes `gradle-wrapper.jar` porque lo generaste una vez, puedes saltar este paso).
2. Desde `backend/` ejecutar:
   ```bash
   ./gradlew bootRun
   ```
3. Endpoints disponibles:
   - `POST /api/citas` crear cita.
   - `GET /api/citas` listar próximas citas.
   - `GET /api/barberos` barberos sembrados.
   - `POST /api/clientes` y `GET /api/clientes` para registro y consulta.

### Frontend (Angular)
1. Instalar Node.js LTS.
2. Desde `frontend/barberia-spa` instalar dependencias y arrancar (usa `proxy.conf.json` para apuntar al backend en `localhost:8080`):
   ```bash
   npm install
   npm start
   ```
   Por defecto corre en `http://localhost:4200` y consume los endpoints anteriores.

## Buenas prácticas y pruebas
- Aplicar TDD en dominio y aplicación; documentar contratos de API y ejecutar pruebas E2E para flujos clave.
- Mantener separación de capas (dominio, aplicación, infraestructura) y usar eventos de dominio para desacoplar contextos.
- Configurar CI/CD con compilación, pruebas, análisis estático y despliegues contenedorizados.

## Referencias
La especificación completa de módulos, planes, endpoints y roadmap se encuentra en `docs/arquitectura.md`.
