# Frontend (Angular)

SPA/PWA en Angular alineada a los contextos de dominio.

## Requerimientos
- Node.js LTS + npm.
- Angular CLI (`npm install -g @angular/cli`).

## Crear el proyecto base
```bash
ng new barberia-spa --routing --style=scss --standalone
cd barberia-spa
ng add @angular/pwa
```

## Estructura sugerida
```
src/app/
  core/              # auth, interceptors, guards
  shared/            # componentes y pipes reutilizables
  features/
    citas/
      pages/ components/ services/ models/ store/
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

## Próximos pasos
- Configurar rutas por rol/plan, guards y layouts compartidos.
- Incorporar diseño responsive y accesibilidad.
- Preparar CI/CD con `npm run lint`, `npm run test` y `npm run e2e`.
