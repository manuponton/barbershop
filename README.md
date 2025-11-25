# barbershop
Diseño de un SaaS para barberías por suscripción
1. Objetivo y alcance
El objetivo es diseñar un software como servicio (SaaS) para barberías que comercialice planes de suscripción. El sistema permitirá a los establecimientos organizar citas, gestionar barberos y clientes, llevar el inventario y la caja, generar informes y ofrecer funcionalidades premium escalonadas según el plan. La solución se construirá con enfoque profesional, aplicando Domain‑Driven Design (DDD), arquitectura hexagonal, CQRS, principios SOLID, buenas prácticas de Clean Code y desarrollo guiado por pruebas (TDD). Se orienta a un equipo de desarrolladores y arquitectos que necesitan un diseño listo para iniciar la implementación.
2. Arquitectura general del sistema
2.1 Visión de alto nivel
Se propone un monolito modular en Java como backend, estructurado mediante arquitectura hexagonal. Cada módulo del dominio es independiente de la infraestructura y expone puertos (interfaces) para permitir adaptadores de entrada (API REST, eventos) y salida (bases de datos, pasarelas de pago, mensajería). El frontend es una aplicación Angular en forma de SPA (PWA) que consume los servicios mediante HTTP/JSON y websockets.
Se utilizan los patrones CQRS y eventos de dominio. Las operaciones de lectura y escritura se separan, manteniendo modelos específicos para cada caso. El artículo de Microsoft destaca que CQRS separa consultas (sin efectos secundarios) y comandos (que cambian el estado)[1], permitiendo que cada capa utilice su propio modelo y tecnología[2]. La guía de patrones avanzados combina Clean Architecture y CQRS para lograr escalabilidad y auditabilidad[3].
2.2 Diagrama conceptual (alto nivel)
El siguiente diagrama describe la interacción entre las capas y los contextos del dominio utilizando notación de puertos y adaptadores. Se presenta en formato Mermaid para facilitar su interpretación. Cada contexto representa un bounded context de DDD.
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
3. Módulos principales y planes de suscripción
El sistema se organiza en módulos (contextos) con funcionalidades escalonadas por plan. Los planes se presentan en orden creciente: Básico, Pro y Premium. Cada módulo es independiente a nivel de dominio y puede activarse o desactivarse según el plan.
3.1 Contexto Citas
•	Propósito: gestionar agendas, reservas y cancelaciones. Inspirado en las plataformas comerciales que ofrecen maximizar la capacidad de reservas y manejar distintos canales[4].
•	Submódulos:
•	Agenda: calendario por barbero y sucursal, reglas de disponibilidad, slots, validación de solapamientos. Permite manejo de citas presenciales y a domicilio.
•	Espera/Walk‑ins (Pro en adelante): lista de espera y kiosco de auto‑check‑in para clientes sin cita previa, similar a las soluciones que permiten gestionar walk‑ins[5].
•	Recordatorios: envío de notificaciones por email/SMS/push recordando la cita, depósitos y políticas de cancelación. Funciones como depósitos de seguridad y cargos por no presentarse se observan en soluciones comerciales[6].
•	Gestión de canales: integración con el sitio web, app móvil y QR para reservar desde distintos canales[7].
3.2 Contexto Barberos (Staff)
•	Propósito: administración del equipo de barberos, horarios de trabajo y comisiones.
•	Submódulos:
•	Gestión de perfiles: datos personales, habilidades, fotografía y rol. Permite mostrar barberos destacados en la app/web.
•	Horarios y vacaciones: configuración individual de disponibilidad, excepciones y ausencias.
•	Comisiones y pagos: cálculo de comisiones por servicio, propinas y seguimiento de ingresos; se integra con el módulo de caja para el pago de nómina.
•	Control de acceso: roles y permisos para barberos, recepcionistas y administradores.
3.3 Contexto Clientes
•	Propósito: gestión del registro de clientes, historial y fidelización.
•	Submódulos:
•	Registro y perfil: creación de clientes, datos de contacto, preferencias, notas de estilos previos.
•	Historial de servicios: lista de citas pasadas, barberos preferidos, productos comprados y facturas.
•	Marketing y fidelidad (Premium): campañas de marketing, programas de fidelidad y tarjetas regalo. Las aplicaciones comerciales ofrecen programas de fidelidad y marketing[8].
•	Gestión de reseñas (Premium): permitir que los clientes dejen valoraciones y opiniones, lo cual puede integrarse con redes sociales.
3.4 Contexto Inventario (disponible a partir del plan Pro)
•	Propósito: gestionar productos, suministros y ventas de artículos. La competencia ofrece e‑commerce e inventario[9].
•	Submódulos:
•	Catálogo de productos: definición de productos (por ejemplo, ceras, champús, aceites), categorías, precios y stock.
•	Gestión de stock: entradas y salidas, ajustes, alertas de bajo inventario y control por sucursal.
•	Ventas minoristas: venta de productos a clientes durante la cita o a través de la tienda en línea, con integración de pagos.
•	Proveedores y compras: órdenes de compra a proveedores, recepción y registro de productos.
3.5 Contexto Caja (disponible a partir del plan Pro)
•	Propósito: administrar transacciones de pago, facturación y flujo de caja.
•	Submódulos:
•	Cobros en punto de venta: facturas de servicios y productos, gestión de métodos de pago y emisión de recibos. Se incluye la posibilidad de aceptar depósitos y cargos por cancelación[6].
•	Movimientos de caja: apertura, cierre y arqueo; ingresos y egresos de efectivo y tarjetas; conciliación diaria.
•	Integración con pasarela de pago: procesar pagos online (tarjeta, wallet), devoluciones y reembolsos.
•	Nómina (Premium): calcular y transferir comisiones a barberos y colaboradores.
3.6 Contexto Reportes (Pro y Premium)
•	Propósito: ofrecer información operativa y analítica.
•	Submódulos:
•	Reportes operativos: citas por barbero, ingresos diarios, servicios más vendidos, indicadores de ocupación.
•	Reportes de clientes: nuevos clientes, retención, recencia/frecuencia (RFM), satisfacción (valoraciones).
•	Reportes financieros: ventas por producto, márgenes, comisiones, cancelaciones y no‑shows.
•	Dashboards avanzados (Premium): cuadros de mando interactivos con filtros por período, barbero, sucursal y comparativas interanuales. Se pueden integrar KPIs de marketing y fidelización.
3.7 Servicios Transversales
Algunos servicios son transversales a varios contextos:
•	Autenticación y autorización: login con email/social, control de accesos y permisos. Uso de OAuth 2.0/JWT.
•	Notificaciones: envío de emails, SMS y notificaciones push (adaptador de salida). Permite recordatorios de citas y avisos de stock.
•	Mensajería y eventos de dominio: bus de mensajes para desacoplar la comunicación entre contextos. Ejemplo: al crear una cita se genera un evento CitaCreada que se procesa para enviar recordatorios y actualizar reportes.
•	Configuración multi‑sucursal (Premium): cada sucursal tiene su propio catálogo, horarios y caja. Permite a cadenas de barberías operar bajo la misma plataforma.
4. Diseño de dominio usando DDD
4.1 Bounded Contexts y Ubiquitous Language
Se identifican los contextos Citas, Barberos, Clientes, Inventario, Caja y Reportes como contextos delimitados de DDD. Cada contexto define su lenguaje ubicuo y sus reglas. La literatura sobre DDD subraya que cada contexto debe tener límites explícitos y modelos específicos[10]. Los elementos clave son entidades, value objects, agregados, repositorios y eventos de dominio[11].
4.2 Entidades, Value Objects y Agregados
•	Entidades: poseen identidad y ciclo de vida. El artículo de Without Debugger señala que una entidad tiene un identificador único y puede modificarse en el tiempo[12]. Ejemplos: Barbero, Cliente, Producto, Cita, Factura.
•	Value Objects: carecen de identidad y se identifican por sus valores; son inmutables[13]. Ejemplos: Horario (inicio, fin), Dirección (calle, ciudad, código postal), Precio (monto, moneda).
•	Agregados: agrupan entidades y value objects como una unidad de consistencia. Un agregado tiene una raíz que controla el acceso a sus elementos internos[14]. Ejemplos: Cita (raíz) incluye la entidad Cliente, el Barbero, el servicio y el horario; PedidoInventario (raíz) contiene productos y cantidades.
4.3 Servicios de dominio y eventos
Cuando una operación no encaja naturalmente en una entidad o value object, se implementa un servicio de dominio. Por ejemplo, PlanificadorDeCitas aplica reglas de negocio complejas (políticas de disponibilidad, depósitos, penalizaciones). Los eventos de dominio comunican cambios significativos; e.g., CitaCreada, PagoRegistrado, ProductoAgotado. La arquitectura hexagonal emplea eventos para desacoplar módulos[15].
4.4 Contextos y sus agregados principales
Contexto	Agregado raíz	Entidades & Value Objects clave	Servicios de dominio
Citas	Cita	Cliente (referencia), Barbero (referencia), Servicio, Horario (VO), EstadoCita (VO)	PlanificadorDeCitas (comprueba solapamientos, crea cita), GestorEsperas
Barberos	Barbero	HorarioTrabajo (VO), Comision (VO), Rol (VO)	CalculadorComisiones
Clientes	Cliente	DatosContacto (VO), Preferencias (VO), TarjetaRegalo	GestorFidelidad (Premium), GestorMarketing
Inventario	Producto	Stock (VO), Precio (VO), Proveedor	GestorInventario (ajuste de stock), GestorCompras
Caja	Factura	MovimientoCaja, Pago (VO), DetalleFactura	ProcesadorPagos (interactúa con pasarela), CalculadorNominas
Reportes	Informe	N/A (los reportes son proyecciones de datos)	GeneradorInformes
5. Diseño del backend con CQRS y eventos de dominio
5.1 Separación de comandos y consultas
El patrón CQRS separa los modelos para leer y escribir datos: las consultas devuelven datos sin efectos secundarios y los comandos modifican el estado[1]. Cada capa mantiene su propio modelo[2] y se gestiona mediante buses de comandos y consultas. Los beneficios incluyen escalabilidad, flexibilidad y optimización de consultas.
En este diseño:
•	Comandos: se implementan como objetos inmutables que representan la intención del usuario, por ejemplo, CrearCitaCommand, CancelarCitaCommand, RegistrarClienteCommand, RegistrarPagoCommand. Cada comando tiene un handler en la capa de aplicación (CrearCitaHandler) que invoca el servicio de dominio correspondiente y publica un evento.
•	Consultas: se implementan como objetos que especifican criterios de lectura (ListarCitasQuery, ObtenerInventarioQuery) y tienen un handler que lee de una proyección. Las proyecciones pueden utilizar una base de datos de lectura optimizada (por ejemplo, tablas desnormalizadas) para mejorar el rendimiento.
5.2 Eventos de dominio y mensajería
Cuando un comando cambia el estado del dominio, se generan eventos (por ejemplo, CitaCreada, ProductoActualizado). Los eventos se envían al bus de mensajería para notificar a otros módulos. El artículo sobre combinaciones de arquitecturas resalta que los eventos de dominio permiten desacoplar módulos y facilitar la escalabilidad[16].
Los suscriptores (handlers de eventos) realizan tareas secundarias, como enviar recordatorios, actualizar reportes o sincronizar el inventario. Se recomienda utilizar un mensaje en cola (RabbitMQ, Kafka) y un mecanismo outbox para asegurar la entrega.
5.3 Persistencia y repositorios
Cada agregado tiene su Repositorio en la capa de infraestructura, que implementa el puerto de persistencia y delega en ORMs o consultas SQL. Los repositorios son inyectados en los handlers mediante inversión de dependencias. La arquitectura hexagonal promueve que los puertos definan la interfaz y los adaptadores la implementación[17].
5.4 Consistencia y transacciones
Se adoptan transacciones por agregado: un comando opera dentro de un solo agregado y se asegura la consistencia interna. La interacción entre agregados se realiza de forma eventual mediante eventos. Esto simplifica la escalabilidad y evita transacciones distribuidas.
5.5 Ejemplo de comando y handler (Java)
// Command: CrearCitaCommand
public record CrearCitaCommand(UUID clienteId, UUID barberoId, LocalDateTime inicio,
                               Duration duracion, String servicio, BigDecimal deposito) {}

// Handler
@Component
public class CrearCitaHandler implements CommandHandler<CrearCitaCommand, UUID> {
    private final CitaRepository citaRepository;
    private final PlanificadorDeCitas planificador;
    private final EventPublisher eventPublisher;

    public UUID handle(CrearCitaCommand command) {
        // verificar disponibilidad y reglas de negocio
        Cita nueva = planificador.crearCita(command.clienteId(), command.barberoId(),
                                            command.inicio(), command.duracion(),
                                            command.servicio(), command.deposito());
        citaRepository.save(nueva);
        eventPublisher.publish(new CitaCreadaEvent(nueva.getId()));
        return nueva.getId();
    }
}
5.6 Ejemplo de query y handler (Java)
// Query: ListarCitasQuery
public record ListarCitasQuery(UUID barberoId, LocalDate fecha) {}

// Handler
@Component
public class ListarCitasHandler implements QueryHandler<ListarCitasQuery, List<CitaDto>> {
    private final CitasReadModel readModel;

    public List<CitaDto> handle(ListarCitasQuery query) {
        return readModel.obtenerCitasPorBarberoYFecha(query.barberoId(), query.fecha());
    }
}
6. Estrategia de TDD (Test‑Driven Development)
El Desarrollo Guiado por Pruebas consiste en escribir primero la prueba automatizada, luego el código mínimo para que la prueba pase y finalmente refactorizar[18]. Este ciclo Rojo–Verde–Refactor se repite para cada funcionalidad. Las leyes de TDD indican que no se escribe código de producción sin un test que falle y que solo se escribe el código necesario para pasar el test[19]. Entre sus beneficios destacan la mejora en la calidad del software, la reducción de errores y la creación de un diseño mantenible[20].
6.1 Niveles de pruebas en el backend
1.	Pruebas de unidad del dominio: se testean entidades, value objects y servicios de dominio en aislamiento. Por ejemplo, probar que PlanificadorDeCitas rechaza solapamientos o aplica penalizaciones. Se aplican mocks a repositorios.
2.	Pruebas de integración de la capa de aplicación: verificar que un handler de comando coordina correctamente el dominio y persiste datos. Se utilizan bases en memoria y se aseguran transacciones.
3.	Pruebas de contrato del API: utilizando herramientas como Spring REST Docs para documentar el contrato; se validan peticiones y respuestas por plan de suscripción.
4.	Pruebas end‑to‑end (E2E): simulan flujos completos desde la UI hasta la base de datos, utilizando entornos de staging.
6.2 Niveles de pruebas en el frontend
1.	Pruebas de componentes: verificar que los componentes Angular renderizan correctamente según su estado y propiedades.
2.	Pruebas de servicios y estado: pruebas unitarias de servicios y efectos de NgRx; se mockean http services.
3.	Pruebas de integración de módulos: se montan módulos completos (por ejemplo, módulo de citas) y se validan formularios y navegación.
4.	Pruebas de extremo a extremo (E2E) con Cypress: simular la experiencia completa de un usuario agendando una cita, registrando un cliente o generando un reporte.
7. Buenas prácticas aplicadas
7.1 Principios SOLID y Clean Code
Los principios SOLID son un conjunto de reglas de diseño orientado a objetos que ayudan a crear clases y módulos mantenibles[21]. Incluyen: Responsabilidad única (una clase debe tener una sola razón de cambio)[22], Abierto/Cerrado (las clases están abiertas a extensión pero cerradas a modificación), Sustitución de Liskov, Segregación de interfaces y Inversión de dependencias. Estos principios se aplican en todo el diseño: cada agregado cumple un único propósito y se extiende mediante herencia o composición; se segregan interfaces para repositorios y adaptadores; y se invierten dependencias para permitir inyectar puertos.
La práctica de Clean Code promueve nombres claros, métodos pequeños, manejo explícito de errores y eliminación de duplicidad. La guía de principios SOLID destaca que el objetivo común es crear código comprensible, legible y comprobable en el que muchos desarrolladores puedan trabajar[23].
7.2 Arquitectura hexagonal y separación de capas
La arquitectura hexagonal desacopla el núcleo de dominio de la infraestructura. Consiste en tres elementos: núcleo con modelos y reglas, puertos que definen interfaces de entrada y salida, y adaptadores que implementan esos puertos[17]. Permite sustituir la persistencia o los canales de comunicación sin modificar el dominio y facilita las pruebas[24]. Se evita la confusión entre DDD y microservicios: no es necesario dividir en microservicios para aplicar DDD[25]; se puede comenzar con un monolito modular y migrar a servicios independientes cuando la escala lo requiera.
7.3 Otros lineamientos
•	Inyección de dependencias: se utiliza para desacoplar componentes y facilitar el testeo.
•	Código auto‑documentado: clases y métodos con nombres claros y sin comentarios redundantes; uso de docs de API generadas automáticamente.
•	Automatización de CI/CD: pipeline con compilación, ejecución de pruebas, análisis de estático, empaquetado y despliegue continuo.
•	Versionamiento de API: los endpoints REST se versionan (ej. /api/v1/...) para permitir cambios no disruptivos.
•	Seguridad: se integran mecanismos de autenticación y autorización; se valida la entrada de datos para prevenir inyección SQL o XSS.
8. Estructura de carpetas (Angular y Java)
8.1 Estructura propuesta para Angular
src/
  app/
    core/           # servicios transversales (auth, guards, interceptors)
    shared/         # componentes y pipes reutilizables
    features/
      citas/
        pages/
        components/
        services/
        models/
        store/      # estado (NgRx)
      barberos/
      clientes/
      inventario/
      caja/
      reportes/
    app-routing.module.ts
    app.module.ts
  assets/
  environments/
Se sugieren módulos de características para cada contexto, respetando el principio de separación de responsabilidades. Los servicios usan RxJS y HTTPClient para comunicarse con la API. Para el manejo de estado global se puede utilizar NgRx (Redux para Angular) o signals en versiones recientes.
8.2 Estructura propuesta para Java (Spring Boot)
src/
  main/
    java/
      com/empresa/barberiasaas/
        citas/
          application/
            command/
            query/
            handler/
            dto/
          domain/
            model/
              Cita.java
              ValueObjects/
            service/
            event/
          infrastructure/
            persistence/
              JpaCitaRepository.java
            messaging/
              CitaEventPublisher.java
            rest/
              CitaController.java
        barberos/
          ...
        clientes/
        inventario/
        caja/
        reportes/
        shared/
          configuration/
          security/
          eventbus/
    resources/
      application.yml
  test/
    java/
      com/empresa/barberiasaas/
        citas/
          domain/
          application/
          infrastructure/
Cada contexto se organiza en application, domain e infrastructure. Las interfaces de los repositorios se definen en la capa de dominio; las implementaciones se ubican en la infraestructura. Las controladoras REST se consideran adaptadores de entrada y viven en la infraestructura. La carpeta shared contiene componentes comunes como configuración, seguridad, utilidades y el bus de eventos.
8.3 Generación del backend con Scaffold Clean Architecture
Para facilitar la implementación de la arquitectura hexagonal y acelerar la creación del backend, se recomienda utilizar el plugin Scaffold Clean Architecture de Bancolombia. Este plugin es un generador de proyectos basado en Gradle que crea automáticamente la estructura de carpetas y módulos siguiendo las mejores prácticas de Clean Architecture[26].
8.3.1 Incorporación del plugin
1.	Cree un directorio nuevo para el proyecto backend y añada un fichero build.gradle. Dentro de este archivo incluya el plugin:
plugins {
    id 'co.com.bancolombia.cleanArchitecture' version '3.26.4'
}
Esto habilitará las tareas que provee el plugin[26].
1.	Ejecute gradle tasks para listar las tareas disponibles. Las tareas principales son:
2.	cleanArchitecture o su alias ca: genera la estructura inicial del proyecto[27].
3.	generateModel o gm: crea una entidad y su interfaz en el módulo domain/model[28].
4.	generateUseCase o guc: añade una clase de caso de uso en el módulo domain/usecase[29].
5.	generateDrivenAdapter o gda: agrega un adaptador de salida (base de datos, cola, etc.) en infrastructure/driven-adapters[30].
6.	generateEntryPoint o gep: crea un adaptador de entrada (REST, Kafka, GraphQL, etc.) en infrastructure/entry-points[31].
8.3.2 Generación del proyecto base
Para iniciar un proyecto de ejemplo llamado BarberiaSaaS con el paquete base com.empresa.barberiasaas y enfoque reactivo, ejecute:
gradle ca --package=com.empresa.barberiasaas --type=reactive --name=BarberiaSaaS
Este comando creará los submódulos applications/app-service, domain/model, domain/usecase, infrastructure/driven-adapters, infrastructure/entry-points, infrastructure/helpers y deployment[32]. Puede elegir el tipo imperative para generar un proyecto basado en Spring Boot MVC en lugar de WebFlux[33].
8.3.3 Generación de módulos adicionales
Una vez creado el proyecto base, el plugin permite generar módulos y artefactos específicos para cada contexto del dominio:
•	Modelo del dominio: ejecutar gradle gm --name=Cita para generar la clase Cita y la interfaz CitaRepository dentro de domain/model[34]. Para cada entidad identificada (Cliente, Barbero, Producto, Factura) se recomienda crear su modelo y repositorio correspondientes.
•	Casos de uso: ejecutar gradle guc --name=RegistrarCita para crear la clase RegistrarCitaUseCase en domain/usecase[35]. Se recomienda generar un caso de uso por cada comando clave (por ejemplo, CancelarCita, RegistrarCliente, RegistrarPago).
•	Adaptadores de salida (driven adapters): utilice gradle gda --type=jpa --name=citas para generar un módulo que implemente el repositorio JPA de citas[30]. El plugin soporta diferentes tecnologías: JPA, MongoDB, Redis, R2DBC, JMS, S3, etc., especificadas con la opción --type[36]. Para cada contexto se elige la tecnología adecuada (por ejemplo, gda --type=redis --name=clientes-cache para un caché de clientes).
•	Adaptadores de entrada (entry points): para exponer una API REST reactiva se ejecuta gradle gep --type=webflux --router=true[31]. El plugin también permite generar controladores REST MVC (restmvc), consumidores Kafka (kafka), servicios GraphQL (graphql) y otros tipos de entrada; se seleccionan mediante --type[37]. Si se requiere una API sin anotaciones, se pueden crear routers funcionales (--router=true).
Al utilizar el plugin se garantiza que la estructura del proyecto cumple con los principios de Clean Architecture: dominio sin dependencias externas, aplicación que orquesta casos de uso, infraestructura para adaptadores y deployment para contenedores y pipelines[32].
8.3.4 Ejemplo de flujo de generación
1.	Crear el proyecto base con gradle ca --package=com.empresa.barberiasaas --type=reactive --name=BarberiaSaaS[27].
2.	Generar las entidades principales: gradle gm --name=Cita, gradle gm --name=Cliente, gradle gm --name=Barbero, gradle gm --name=Producto, gradle gm --name=Factura[34].
3.	Generar casos de uso: gradle guc --name=CrearCita, gradle guc --name=CancelarCita, gradle guc --name=RegistrarCliente, gradle guc --name=CrearProducto, gradle guc --name=RegistrarPago[35].
4.	Generar adaptadores JPA y REST: gradle gda --type=jpa --name=citas, gradle gda --type=jpa --name=clientes, gradle gda --type=jpa --name=barberos para la persistencia[30], y gradle gep --type=webflux --router=true para exponer endpoints reactivos[31].
5.	Ajustar el código generado según los agregados y servicios diseñados previamente; implementar reglas de negocio en los casos de uso y eventos de dominio.
El uso de Scaffold Clean Architecture provee una base sólida y automatiza la creación de módulos, reduciendo la posibilidad de errores y asegurando que se sigan las convenciones de Clean Architecture en todo el proyecto.
9. API REST y comandos/consultas
9.1 Endpoints (REST)
Se expone una API REST versionada (/api/v1) con endpoints que siguen convenciones RESTful y devuelven códigos de estado HTTP claros. Los endpoints están segregados por contexto y plan.
Contexto / Recurso	Método	Endpoint	Descripción
Citas	POST	/api/v1/citas	Crear una cita. Recibe CrearCitaCommand.
	GET	/api/v1/citas?barberoId=&fecha=	Listar citas por barbero y fecha (query).
	GET	/api/v1/citas/{citaId}	Obtener detalle de una cita.
	PUT	/api/v1/citas/{citaId}/cancelar	Cancelar una cita, generando penalización si aplica.
	POST	/api/v1/citas/{citaId}/checkin	Registrar check‑in de cliente (Pro/Premium).
Barberos	POST	/api/v1/barberos	Registrar un barbero.
	GET	/api/v1/barberos	Listar barberos.
	GET	/api/v1/barberos/{id}	Obtener detalle de barbero.
	PUT	/api/v1/barberos/{id}	Actualizar datos de barbero.
Clientes	POST	/api/v1/clientes	Registrar cliente.
	GET	/api/v1/clientes/{id}	Obtener perfil de cliente.
	GET	/api/v1/clientes	Búsqueda y listado de clientes.
	PUT	/api/v1/clientes/{id}	Actualizar datos del cliente.
Inventario	POST	/api/v1/productos	Crear producto (Pro/Premium).
	GET	/api/v1/productos	Listar productos (filtro por stock).
	PUT	/api/v1/productos/{id}	Actualizar producto (precio, stock).
	POST	/api/v1/compras	Registrar orden de compra a proveedor.
Caja	POST	/api/v1/facturas	Crear factura de servicios/productos.
	POST	/api/v1/pagos	Registrar pago (tarjeta/efectivo/deposito).
	GET	/api/v1/caja/arqueo	Consultar arqueo de caja del día.
	POST	/api/v1/caja/cierre	Cerrar caja diaria (Pro/Premium).
Reportes	GET	/api/v1/reportes/ventas	Consultar reporte de ventas por periodo (Pro/Premium).
	GET	/api/v1/reportes/ocupacion	Consultar ocupación por barbero y sucursal.
	GET	/api/v1/reportes/clientes	Reporte de clientes nuevos y recurrentes.
9.2 Comandos y Queries principales
Comando / Query	Contexto	Descripción
CrearCitaCommand	Citas	Crea una nueva cita con cliente, barbero, horario y servicio.
CancelarCitaCommand	Citas	Cancela una cita y calcula penalizaciones.
RegistrarClienteCommand	Clientes	Crea un nuevo cliente.
CrearProductoCommand	Inventario	Registra un producto en el inventario.
AjustarStockCommand	Inventario	Actualiza el stock de un producto.
RegistrarFacturaCommand	Caja	Genera una factura con servicios y productos vendidos.
RegistrarPagoCommand	Caja	Procesa un pago y actualiza el estado de la factura.
GenerarReporteVentasQuery	Reportes	Devuelve ventas por periodo, agrupadas por sucursal o barbero.
ListarCitasQuery	Citas	Lista las citas filtradas por barbero y fecha.
ObtenerClienteQuery	Clientes	Devuelve detalles de un cliente.
10. Tabla comparativa de planes de suscripción
Funcionalidad clave	Básico	Pro	Premium
Gestión de citas (crear, editar, cancelar)	✔︎	✔︎	✔︎
Agenda por barbero y disponibilidad	✔︎	✔︎	✔︎
Gestión de barberos (perfiles, horarios)	✔︎	✔︎	✔︎
Gestión de clientes y perfiles	✔︎	✔︎	✔︎
Notificaciones por email/SMS/push	✔︎	✔︎	✔︎
Lista de espera y auto‑check‑in kiosco	✖︎	✔︎	✔︎
Gestión de inventario y productos	✖︎	✔︎	✔︎
Punto de venta (facturación y caja)	✖︎	✔︎	✔︎
Reportes básicos (ingresos, citas)	✖︎	✔︎	✔︎
Reportes avanzados y dashboards interactivos	✖︎	✖︎	✔︎
Marketing y campañas de fidelidad	✖︎	✖︎	✔︎
Programa de puntos, tarjetas regalo	✖︎	✖︎	✔︎
Multi‑sucursal y configuración regional	✖︎	✖︎	✔︎
Tienda en línea y e‑commerce integrado	✖︎	✖︎	✔︎
Personalización de marca (app y sitio web)	✖︎	✖︎	✔︎
11. Roadmap de desarrollo y plan de escalabilidad
11.1 Fases del roadmap
1.	MVP (Básico) – 0–3 meses
2.	Implementar los contextos de Citas, Barberos y Clientes con operaciones CRUD básicas y control de disponibilidad. Exponer API REST y módulos de UI en Angular.
3.	Integrar autenticación y roles mínimos (propietario, barbero, recepcionista).
4.	Configurar pipeline CI/CD, pruebas automatizadas (TDD) y despliegue en la nube (Docker + Kubernetes o plataforma PaaS).
5.	Fase Pro – 4–6 meses
6.	Añadir contextos de Inventario, Caja y Reportes con sus comandos y queries. Integrar pasarela de pago para cobros online y POS físico.
7.	Implementar lista de espera y kiosco de auto‑check‑in como módulo de UI adicional, inspirado en soluciones que permiten waitlists y kioscos[38].
8.	Incorporar recordatorios mediante email/SMS y depósitos por cancelaciones[6].
9.	Empezar a separar los modelos de lectura utilizando CQRS; crear proyecciones para reportes y paneles.
10.	Fase Premium – 7–12 meses
11.	Incorporar marketing y fidelización: campañas segmentadas, programa de puntos y tarjetas regalo.
12.	Crear módulo de multi‑sucursal y personalización de marca (logo, colores, URL propia) para app/web.
13.	Desarrollar tienda en línea con carrito, métodos de envío y pago; integrar con inventario y caja.
14.	Implementar dashboards avanzados y analítica con herramientas de BI; integrar Google Analytics y CRMs externos.
15.	Ofrecer SDK o API pública para integraciones de terceros.
16.	Fase de escalabilidad y microservicios – >12 meses
17.	Evaluar la carga y, si es necesario, extraer contextos con alta demanda (Citas y Caja) a microservicios independientes. La literatura subraya que DDD y la arquitectura hexagonal permiten dividir hacia microservicios cuando sea necesario, pero no requieren microservicios desde el inicio[25].
18.	Adoptar bases de datos separadas para lectura y escritura, replicación y particionamiento para soportar miles de citas diarias.
19.	Implementar Event Sourcing en contextos donde la auditoría sea crítica (Caja e Inventario) para reconstruir el estado del sistema[39].
20.	Automatizar escalamientos horizontales y monitorización (Prometheus, Grafana) y centralizar logs.
11.2 Estrategia de escalabilidad
1.	Monolito modular: comenzar con un monolito bien estructurado facilita la cohesión del código y reduce la complejidad operativa. Se deja la puerta abierta a extraer microservicios.
2.	Separación de lectura y escritura: aplicar CQRS y proyecciones para reportes permite escalar lecturas de forma independiente.
3.	Eventos y mensajería: los eventos de dominio se publican en un bus, permitiendo añadir nuevos suscriptores sin modificar el código existente[16].
4.	Infraestructura en la nube: usar contenedores y orquestadores para escalar horizontalmente; diseñar para tolerancia a fallos (bases de datos replicadas, servicios redundantes).
5.	Seguridad y cumplimiento: implementar prácticas de seguridad desde el inicio (cifrado de datos sensibles, autenticación robusta) y cumplir normativas locales (protección de datos de clientes).
12. Conclusiones
El presente documento detalla la arquitectura, dominio, módulos, planes, prácticas y roadmap para un SaaS de barberías por suscripción. Aplicar DDD, arquitectura hexagonal, CQRS, SOLID y TDD permite construir un sistema mantenible y escalable. Comenzar con un monolito modular y evolucionar hacia microservicios según la demanda proporciona flexibilidad y evita la complejidad prematura. Los planes progresivos (Básico, Pro y Premium) permiten monetizar gradualmente y ofrecer valor real a los clientes.
________________________________________
[1] [2] Aplicación de patrones CQRS y DDD simplificados en un microservicio - .NET | Microsoft Learn
https://learn.microsoft.com/es-es/dotnet/architecture/microservices/microservice-ddd-cqrs-patterns/apply-simplified-microservice-cqrs-ddd-patterns
[3] [15] [16] [39] Arquitectura de Software con C# 28: Patrones Avanzados y Combinaciones Entre Arquitecturas – Without Debugger
https://withoutdebugger.com/2025/03/22/arquitectura-de-software-con-c-28-patrones-avanzados-y-combinaciones-entre-arquitecturas/
[4] [5] [6] [7] [8] [9] [38] Barbershop software. CRM for barbershops
https://www.barberly.com/barbershop-software
[10] [11] [17] [24] [25] No necesitas microservicios para utilizar la arquitectura hexagonal y el DDD - Apiumhub
https://apiumhub.com/es/tech-blog-barcelona/arquitectura-hexagonal-y-el-ddd/
[12] [13] [14] Arquitectura de Software con C# 05: Tácticas y Patrones de DDD (Entidades, Agregados, Value Objects) – Without Debugger
https://withoutdebugger.com/2024/11/09/arquitectura-de-software-con-c-05-tacticas-y-patrones-de-ddd-entidades-agregados-value-objects/
[18] [19] [20] Desarrollo Guiado por Pruebas (TDD): Guía completa
https://www.codurance.com/es/guia-completa-desarrollo-guiado-por-pruebas-tdd
[21] [22] [23] Los principios SOLID de programación orientada a objetos explicados en Español sencillo
https://www.freecodecamp.org/espanol/news/los-principios-solid-explicados-en-espanol/
[26] Getting Started | Scaffold Clean Architecture
https://bancolombia.github.io/scaffold-clean-architecture/docs/getting-started/
[27] [32] [33] Generate Project | Scaffold Clean Architecture
https://bancolombia.github.io/scaffold-clean-architecture/docs/tasks/generate-project/
[28] [34] Generate Model | Scaffold Clean Architecture
https://bancolombia.github.io/scaffold-clean-architecture/docs/tasks/generate-model/
[29] [35] Generate Use Case | Scaffold Clean Architecture
https://bancolombia.github.io/scaffold-clean-architecture/docs/tasks/generate-use-case/
[30] [36] Generate Driven Adapter | Scaffold Clean Architecture
https://bancolombia.github.io/scaffold-clean-architecture/docs/tasks/generate-driven-adapter/
[31] [37] Generate Entry Point | Scaffold Clean Architecture
https://bancolombia.github.io/scaffold-clean-architecture/docs/tasks/generate-entry-point/
