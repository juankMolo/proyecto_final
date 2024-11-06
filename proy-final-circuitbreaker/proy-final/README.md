Proyecto Final: Implementación de Circuit Breaker, Chaos Monkey y Prometheus con Docker Compose
Este proyecto implementa un patrón de diseño Circuit Breaker con Resilience4j para gestionar fallos, Chaos Monkey para simular fallos controlados, y Prometheus junto con Grafana para el monitoreo de métricas en un entorno de microservicios con Docker Compose.

Requisitos Previos
Asegúrate de tener instalado lo siguiente:

Docker: https://docs.docker.com/get-docker/
Docker Compose: https://docs.docker.com/compose/install/
Maven (para construir la aplicación): https://maven.apache.org/install.html
Estructura del Proyecto
El proyecto contiene dos microservicios, frontend y microusers, que serán monitoreados y gestionados con Circuit Breaker y Chaos Monkey. Además, Prometheus y Grafana se utilizan para recolectar y visualizar métricas.

Pasos para la Implementación
Paso 1: Configurar los Microservicios para Circuit Breaker y Prometheus
1.1 Configuración de application.properties
En cada microservicio (frontend y microusers), configura el archivo application.properties para habilitar los endpoints de Actuator y las propiedades de Circuit Breaker de Resilience4j.

# Configuración del puerto
server.port=8080  # Cambia a 8081 para microusers

# Exposición de Actuator y Prometheus
management.endpoints.web.exposure.include=*
management.endpoint.prometheus.enabled=true

# Configuración de Resilience4j para Circuit Breaker
resilience4j.circuitbreaker.instances.microusersCircuitBreaker.registerHealthIndicator=true
resilience4j.circuitbreaker.instances.microusersCircuitBreaker.slidingWindowSize=20
resilience4j.circuitbreaker.instances.microusersCircuitBreaker.permittedNumberOfCallsInHalfOpenState=3
resilience4j.circuitbreaker.instances.microusersCircuitBreaker.waitDurationInOpenState=10000
resilience4j.circuitbreaker.instances.microusersCircuitBreaker.failureRateThreshold=50

1.2 Configuración de Resilience4j en ViewController.java
En el controlador de frontend, añade el Circuit Breaker utilizando la anotación @CircuitBreaker. Define un método de fallback que se ejecutará si el servicio falla.

@CircuitBreaker(name = "microusersCircuitBreaker", fallbackMethod = "fallbackMethod")
public ResponseEntity<String> callMicrousersService(...) {
    // Lógica de llamada al servicio microusers
}

public ResponseEntity<String> fallbackMethod(...) {
    return new ResponseEntity<>("Servicio no disponible, intenta más tarde.", HttpStatus.SERVICE_UNAVAILABLE);
}

1.3 Definir RestTemplate en FrontendApplication.java
Añade un RestTemplate como un @Bean en la clase principal para permitir que el Circuit Breaker gestione las solicitudes HTTP.

@Bean
public RestTemplate restTemplate() {
    return new RestTemplate();
}


Paso 2: Configurar Docker Compose
Crea el archivo docker-compose.yml en el directorio raíz del proyecto para definir los servicios y sus configuraciones.

version: '3.8'
services:
  frontend:
    build: ./frontend
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=default
      - MANAGEMENT_ENDPOINTS_WEB_EXPOSURE_INCLUDE=*
      - MANAGEMENT_ENDPOINT_PROMETHEUS_ENABLED=true
    networks:
      - proy-network

  microusers:
    build: ./microusers
    ports:
      - "8081:8081"
    environment:
      - SPRING_PROFILES_ACTIVE=default
      - MANAGEMENT_ENDPOINTS_WEB_EXPOSURE_INCLUDE=*
      - MANAGEMENT_ENDPOINT_PROMETHEUS_ENABLED=true
    networks:
      - proy-network

  prometheus:
    image: prom/prometheus:latest
    ports:
      - "9090:9090"
    volumes:
      - ./prometheus.yml:/etc/prometheus/prometheus.yml
    networks:
      - proy-network

  grafana:
    image: grafana/grafana:latest
    ports:
      - "3000:3000"
    depends_on:
      - prometheus
    networks:
      - proy-network

networks:
  proy-network:
    driver: bridge

Paso 3: Configurar Prometheus
Crea el archivo prometheus.yml en el directorio raíz para configurar Prometheus y monitorear los servicios frontend y microusers.

global:
  scrape_interval: 15s

scrape_configs:
  - job_name: 'frontend'
    metrics_path: '/actuator/prometheus'
    static_configs:
      - targets: ['frontend:8080']

  - job_name: 'microusers'
    metrics_path: '/actuator/prometheus'
    static_configs:
      - targets: ['microusers:8081']


global:
  scrape_interval: 15s

scrape_configs:
  - job_name: 'frontend'
    metrics_path: '/actuator/prometheus'
    static_configs:
      - targets: ['frontend:8080']

  - job_name: 'microusers'
    metrics_path: '/actuator/prometheus'
    static_configs:
      - targets: ['microusers:8081']


Paso 4: Implementar Chaos Monkey
4.1 Añadir la Dependencia de Chaos Monkey
Añade Chaos Monkey a tus microservicios en el archivo pom.xml:

<dependency>
    <groupId>de.codecentric</groupId>
    <artifactId>chaos-monkey-spring-boot</artifactId>
    <version>2.4.1</version>
</dependency>


4.2 Configurar Chaos Monkey en application.properties
Configura Chaos Monkey para inyectar fallos en los servicios de Spring:

management.endpoints.web.exposure.include=*
management.endpoint.chaosmonkey.enabled=true

chaos.monkey.enabled=true
chaos.monkey.watcher.controller=true
chaos.monkey.assaults.exceptionsActive=true
chaos.monkey.assaults.exceptionProbability=100  # Probabilidad de lanzar una excepción

Paso 5: Construir y Correr los Contenedores
Ejecuta los siguientes comandos para construir y levantar los contenedores de todos los servicios definidos en Docker Compose:

docker-compose down  # Asegúrate de que no haya contenedores corriendo previamente
docker-compose up --build -d


Paso 6: Verificar los Servicios y Métricas
Prometheus: Accede a http://localhost:9090 para verificar que los servicios frontend y microusers están siendo monitoreados. Ve a Status > Targets y verifica que ambos servicios están en estado UP.

Grafana: Accede a http://localhost:3000 para visualizar las métricas. Configura Prometheus como fuente de datos y crea un dashboard personalizado.
Chaos Monkey: Usa el endpoint http://localhost:8080/actuator/chaosmonkey para activar Chaos Monkey y observar cómo afecta al sistema.


Paso 7: Probar el Circuit Breaker
Para probar el Circuit Breaker, realiza múltiples solicitudes a los servicios frontend y microusers para ver cómo responde el sistema cuando se alcanzan los umbrales de fallos configurados.

curl http://localhost:8080/endpoint  # Cambia "endpoint" al endpoint específico que desees probar

Consideraciones Finales
Umbrales de Fallos: Ajusta los valores de failureRateThreshold y slidingWindowSize en application.properties para probar diferentes configuraciones del Circuit Breaker.
Logs: Monitorea los logs de los contenedores para analizar el comportamiento del Circuit Breaker y Chaos Monkey.

Comandos Útiles
Detener y eliminar los contenedores:
docker-compose down


Ver logs de los servicios:
docker-compose logs -f


