# Backend (Java)

Backend ligero con Spring Boot 3 (WebFlux) siguiendo principios de dominio (DDD), arquitectura hexagonal y CQRS. Expone adaptadores REST para los contextos iniciales de citas, barberos y clientes, almacenando los datos en memoria para facilitar la demostración junto al frontend Angular.

## Tecnologías
- Java 17, Spring Boot 3.2
- Gradle Wrapper
- Validaciones con Jakarta Validation

## Ejecutar
El wrapper (`gradle/wrapper/gradle-wrapper.jar`) está ignorado, pero puedes regenerarlo con Gradle 8.14.3 y luego levantar el servicio:

```bash
cd backend
gradle wrapper --gradle-version 8.14.3
./gradlew bootRun
```

Los endpoints están disponibles en `http://localhost:8080` y se consumen desde la SPA con `proxy.conf.json`.

### Endpoints principales
- `POST /api/citas` crea una cita (`clientName`, `barberName`, `service`, `startAt`, `durationMinutes`).
- `GET /api/citas` lista las citas registradas.
- `GET /api/barberos` devuelve un catálogo semilla.
- `POST /api/clientes` registra un cliente (`name`, `email`, `birthday`).
- `GET /api/clientes` lista los clientes registrados.

## Estructura
```
src/main/java/com/empresa/barberia
  api/                  # Controladores REST y DTOs
  domain/               # Modelos de dominio inmutables
  repository/           # Adaptadores en memoria
  service/              # Casos de uso de aplicación
```

## Próximos pasos
- Añadir persistencia real por contexto (JPA/Reactive) y mensajería para eventos.
- Dividir comandos/consultas explícitamente y cubrir con pruebas unitarias y de integración.
