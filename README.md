# BarberShop SaaS

Diseño y guía de arranque para un SaaS de barberías con planes de suscripción (Básico, Pro y Premium). El sistema aplica DDD, arquitectura hexagonal, CQRS, SOLID, Clean Code y TDD.

## Estructura del repositorio
- `docs/arquitectura.md`: detalles completos de arquitectura, módulos, planes y roadmap.
- `backend/`: guía para generar el backend Java con el plugin Scaffold Clean Architecture y estructura por contextos.
- `frontend/`: guía para crear la SPA/PWA en Angular.

## Cómo empezar
### Backend (Java)
1. Instalar JDK y Gradle.
2. Generar la base con el plugin de Clean Architecture:
   ```bash
   gradle ca --package=com.empresa.barberiasaas --type=reactive --name=BarberiaSaaS
   ```
3. Crear modelos, casos de uso y adaptadores por contexto (citas, barberos, clientes, inventario, caja, reportes). Consulta `backend/README.md` para comandos sugeridos.

### Frontend (Angular)
1. Instalar Node.js y Angular CLI.
2. Crear la SPA/PWA:
   ```bash
   ng new barberia-spa --routing --style=scss --standalone
   cd barberia-spa
   ng add @angular/pwa
   ```
3. Estructurar módulos por contexto (citas, barberos, clientes, inventario, caja, reportes). Ver `frontend/README.md`.

## Buenas prácticas y pruebas
- Aplicar TDD en dominio y aplicación; documentar contratos de API y ejecutar pruebas E2E para flujos clave.
- Mantener separación de capas (dominio, aplicación, infraestructura) y usar eventos de dominio para desacoplar contextos.
- Configurar CI/CD con compilación, pruebas, análisis estático y despliegues contenedorizados.

## Referencias
La especificación completa de módulos, planes, endpoints y roadmap se encuentra en `docs/arquitectura.md`.
