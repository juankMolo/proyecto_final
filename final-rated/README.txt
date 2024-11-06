Rate Limiter en el Servicio de Usuarios
Descripción
Este proyecto implementa un mecanismo de Rate Limiting en el servicio UserService para controlar la cantidad de solicitudes que se pueden realizar en un periodo determinado. Usamos la biblioteca Resilience4j y su funcionalidad de RateLimiter para limitar el número de solicitudes en los métodos clave de UserService.

Objetivo
El Rate Limiter ayuda a controlar el flujo de tráfico hacia el servicio, previniendo sobrecargas y asegurando que el sistema maneje solicitudes en un ritmo sostenible. Cuando el límite de solicitudes se supera, el servicio devuelve una respuesta con un código de error 429 Too Many Requests.

Requisitos Previos
Java 11 o superior
Maven
Spring Boot
Resilience4j como dependencia
Configuración y Uso
Paso 1: Añadir Dependencias
Agrega la dependencia de Resilience4j en el archivo pom.xml:

xml
Copiar código
<dependency>
        <groupId>io.github.resilience4j</groupId>
        <artifactId>resilience4j-spring-boot3</artifactId>
        <version>2.0.2</version>
</dependency>
Paso 2: Configuración de Rate Limiter en application.properties
Define los parámetros del Rate Limiter en el archivo application.properties:

properties
Copiar código
resilience4j.ratelimiter.instances.userService.limitForPeriod=1
resilience4j.ratelimiter.instances.userService.limitRefreshPeriod=10s
resilience4j.ratelimiter.instances.userService.timeoutDuration=1s

limitForPeriod=1: Establece un límite de 1 solicitudes en cada período.
limitRefreshPeriod=10s: Reinicia el límite cada 10 segundos.
timeoutDuration=1s: Tiempo máximo que una solicitud esperará antes de lanzar una excepción cuando el límite ha sido alcanzado.
Paso 3: Añadir Rate Limiter en los Métodos de UserService
En UserService.java, aplicamos el RateLimiter con la anotación @RateLimiter para limitar las solicitudes a los métodos del servicio:

java
Copiar código
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;

@Service
public class UserService {

    // Métodos del servicio con limitador de tasa

    @RateLimiter(name = "userService")
    public User authenticate(String username, String password) {
        // lógica de autenticación
    }

    @RateLimiter(name = "userService")
    public List<User> getAllUsers() {
        // lógica para obtener todos los usuarios
    }

    @RateLimiter(name = "userService")
    public User createUser(User user) {
        // lógica para crear un usuario
    }
    
    // Otros métodos con el mismo limitador
}

Conclusión
Con esta implementación, el RateLimiter controla la carga en el servicio, rechazando solicitudes adicionales que excedan el límite establecido. Esto permite gestionar el flujo de tráfico de manera efectiva y asegurar que el sistema maneje las solicitudes de manera sostenible y controlada.
