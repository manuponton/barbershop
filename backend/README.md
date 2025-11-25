# Backend (Java)

Backend con Spring Boot 3 (WebFlux) siguiendo arquitectura hexagonal, DDD y CQRS. Incluye contextos de ejemplo para citas, barberos y clientes con adaptadores REST expuestos en `/api/*`.

## Tecnologías
- Java 17, Spring Boot 3.2
- Gradle Wrapper
- Validaciones con Jakarta Validation

## Ejecutar
Genera el wrapper localmente (no versionamos `gradle-wrapper.jar` para evitar binarios en el repo) y luego arranca el servicio:
```bash
gradle wrapper --gradle-version 8.14.3
./gradlew bootRun
```

### Endpoints principales
- `POST /api/citas` crea una cita (`clientName`, `barberName`, `service`, `startAt`, `durationMinutes`).
- `GET /api/citas` lista citas futuras desde ahora o un `from` opcional.
- `GET /api/barberos` devuelve un catálogo semilla.
- `POST /api/clientes` registra un cliente (`name`, `email`, `birthday`).
- `GET /api/clientes` lista los clientes registrados.

## Estructura
```
backend/
  src/main/java/com/empresa/barberiasaas/
    citas/         # dominio y servicio de citas + controlador REST
    barberos/      # catálogo semilla de barberos
    clientes/      # registro en memoria de clientes
    shared/        # espacio para utilidades comunes
```

## Próximos pasos
- Añadir persistencia real por contexto (JPA/Reactive) y mensajería para eventos.
- Dividir comandos/consultas explícitamente y cubrir con pruebas unitarias y de integración.
