🏗️ Descripción

Este proyecto implementa una arquitectura de microservicios para la gestión de una biblioteca digital. Cada servicio cumple una función específica dentro del ecosistema, garantizando modularidad, escalabilidad y resiliencia.

⚙️ Componentes

API Gateway: gestiona las peticiones, aplica políticas de rate limiting y se integra con Redis.

Discovery Server (Eureka): registro y descubrimiento dinámico de microservicios.

Book Service: maneja la información de libros y catálogos.

Author Service: administración de autores y sus publicaciones.

User Service: autenticación y administración de usuarios con JWT.

Loan Service: gestión de préstamos y devoluciones.

Review Service: módulo para calificaciones y reseñas de usuarios.

🧩 Tecnologías principales

Spring Boot, Spring Cloud Gateway, Eureka, Redis, Kafka, Zipkin, Prometheus, Grafana

PostgreSQL, MySQL, Redis y Mongodb para persistencia de datos.

Docker Compose para orquestación y despliegue.

🔍 Características clave

Arquitectura distribuida y desacoplada.

Comunicación asíncrona con Kafka.

Seguridad basada en JWT.

Observabilidad con Zipkin y Grafana.

Configuración centralizada con Spring Cloud Config Server.
