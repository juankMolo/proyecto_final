// frontend/src/main/java/com/example/frontend/controller/ViewController.java
package com.example.frontend.controller;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Controller
public class ViewController {

    @Autowired
    private RestTemplate restTemplate;

    private static final String MICROUSERS_CIRCUIT_BREAKER = "microusersCircuitBreaker";

    // Página de inicio de sesión
    @GetMapping("/")
    public String index() {
        return "index";
    }

    // Página de usuarios
    @GetMapping("/users")
    public String users() {
        return "users";
    }

    // Método para manejar el inicio de sesión
    @PostMapping("/login")
    @ResponseBody
    @CircuitBreaker(name = MICROUSERS_CIRCUIT_BREAKER, fallbackMethod = "loginFallback")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String url = "http://192.168.50.3:8081/api/login";
        try {
            return restTemplate.postForEntity(url, credentials, String.class);
        } catch (Exception e) {
            throw e; // Asegúrate de lanzar la excepción para que el Circuit Breaker la detecte
        }
    }

    // Método de fallback para login
    public ResponseEntity<?> loginFallback(@RequestBody Map<String, String> credentials, Throwable throwable) {
        return ResponseEntity.status(503).body("Servicio de autenticación no disponible. Por favor, inténtalo más tarde.");
    }

    // Método para obtener usuarios
    @GetMapping("/getUsers")
    @ResponseBody
    @CircuitBreaker(name = MICROUSERS_CIRCUIT_BREAKER, fallbackMethod = "getUsersFallback")
    public ResponseEntity<?> getUsers() {
        String url = "http://192.168.50.3:8081/api/users";
        try {
            return restTemplate.getForEntity(url, String.class);
        } catch (Exception e) {
            throw e;
        }
    }

    // Método de fallback para getUsers
    public ResponseEntity<?> getUsersFallback(Throwable throwable) {
        return ResponseEntity.status(503).body("No se pueden obtener los usuarios en este momento. Por favor, inténtalo más tarde.");
    }

    // Método para crear un usuario
    @PostMapping("/createUser")
    @ResponseBody
    @CircuitBreaker(name = MICROUSERS_CIRCUIT_BREAKER, fallbackMethod = "createUserFallback")
    public ResponseEntity<?> createUser(@RequestBody Map<String, String> user) {
        String url = "http://192.168.50.3:8081/api/users";
        try {
            return restTemplate.postForEntity(url, user, String.class);
        } catch (Exception e) {
            throw e;
        }
    }

    // Método de fallback para crear usuario
    public ResponseEntity<?> createUserFallback(@RequestBody Map<String, String> user, Throwable throwable) {
        return ResponseEntity.status(503).body("No se pudo crear el usuario. Por favor, inténtalo más tarde.");
    }

    // Método para actualizar un usuario
    @PutMapping("/updateUser/{id}")
    @ResponseBody
    @CircuitBreaker(name = MICROUSERS_CIRCUIT_BREAKER, fallbackMethod = "updateUserFallback")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody Map<String, String> user) {
        String url = "http://192.168.50.3:8081/api/users/" + id;
        try {
            restTemplate.put(url, user);
            return ResponseEntity.ok("Usuario actualizado exitosamente.");
        } catch (Exception e) {
            throw e;
        }
    }

    // Método de fallback para actualizar usuario
    public ResponseEntity<?> updateUserFallback(@PathVariable Long id, @RequestBody Map<String, String> user, Throwable throwable) {
        return ResponseEntity.status(503).body("No se pudo actualizar el usuario. Por favor, inténtalo más tarde.");
    }

    // Método para eliminar un usuario
    @DeleteMapping("/deleteUser/{id}")
    @ResponseBody
    @CircuitBreaker(name = MICROUSERS_CIRCUIT_BREAKER, fallbackMethod = "deleteUserFallback")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        String url = "http://192.168.50.3:8081/api/users/" + id;
        try {
            restTemplate.delete(url);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            throw e;
        }
    }

    // Método de fallback para eliminar usuario
    public ResponseEntity<?> deleteUserFallback(@PathVariable Long id, Throwable throwable) {
        return ResponseEntity.status(503).body("No se pudo eliminar el usuario. Por favor, inténtalo más tarde.");
    }
}
