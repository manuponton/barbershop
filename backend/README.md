# Backend (Java)

Base para el backend siguiendo arquitectura hexagonal, DDD y CQRS.

## Tecnologías sugeridas
- Java 17+, Spring Boot (WebFlux o MVC según necesidad).
- Gradle con el plugin [Scaffold Clean Architecture](https://bancolombia.github.io/scaffold-clean-architecture/docs/getting-started/).
- Bases de datos SQL/NoSQL según contexto, mensajería (Kafka/RabbitMQ), JWT/OAuth2 para seguridad.

## Cómo generar la estructura inicial
1. Instalar JDK y Gradle.
2. Ejecutar en la raíz del backend:
   ```bash
   gradle ca --package=com.empresa.barberiasaas --type=reactive --name=BarberiaSaaS
   ```
3. Crear modelos y repositorios por contexto (ejemplos):
   ```bash
   gradle gm --name=Cita
   gradle gm --name=Cliente
   gradle gm --name=Barbero
   gradle gm --name=Producto
   gradle gm --name=Factura
   ```
4. Crear casos de uso alineados a los comandos principales:
   ```bash
   gradle guc --name=CrearCita
   gradle guc --name=RegistrarCliente
   gradle guc --name=RegistrarPago
   ```
5. Generar adaptadores:
   ```bash
   gradle gda --type=jpa --name=citas
   gradle gda --type=jpa --name=clientes
   gradle gda --type=jpa --name=barberos
   gradle gep --type=webflux --router=true
   ```

## Estructura recomendada por contexto
```
backend/
  citas/
    application/command|query|handler|dto
    domain/model|service|event
    infrastructure/persistence|messaging|rest
  barberos/
  clientes/
  inventario/
  caja/
  reportes/
  shared/
```

## Pruebas
- **Unidad de dominio**: entidades, VOs, servicios.
- **Integración de aplicación**: handlers con repositorios en memoria.
- **Contrato de API**: REST Docs o equivalente.
- **E2E**: flujos completos con base temporal y mensajería simulada.

## Pasos siguientes
- Configurar CI/CD con compilación, pruebas y análisis estático.
- Añadir observabilidad (logs estructurados, métricas, tracing) y seguridad (cifrado de secretos, validación de entrada).
