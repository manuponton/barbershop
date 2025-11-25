# Frontend (Angular)

SPA/PWA en Angular alineada a los contextos de dominio del SaaS de barberías. El proyecto consume el backend WebFlux para registrar
clientes, consultar barberos sembrados y agendar citas en vivo desde la interfaz.

## Requerimientos
- Node.js LTS + npm.
- Angular CLI (`npm install -g @angular/cli`) opcional si quieres usar los comandos globales.

## Ejecutar
```bash
cd frontend/barberia-spa
npm install
npm start
```
La aplicación se sirve en `http://localhost:4200` y, gracias al `proxy.conf.json`, reenvía `/api/*` al backend local en
`http://localhost:8080`.

## Funcionalidad actual
- Formulario para registrar clientes y refrescar el catálogo local en pantalla.
- Formulario para agendar citas usando clientes creados y barberos seed (`/api/barberos`).
- Tablas y listas que leen directamente `/api/clientes`, `/api/barberos` y `/api/citas` con botones de refresco.

## Estructura sugerida
```
frontend/barberia-spa/src/app/
  core/              # auth, interceptors, guards
  shared/            # componentes y pipes reutilizables
  features/
    citas/
    barberos/
    clientes/
    inventario/
    caja/
    reportes/
```
Se recomienda NgRx (o signals) para estado, módulos lazy por contexto y comunicación HTTP/JSON con el backend.

## Pruebas
- **Unitarias**: componentes, servicios y pipes con Karma/Jasmine o Vitest.
- **Integración**: módulos feature montados con TestBed.
- **E2E**: Cypress para flujos de reserva, pagos y reportes.
