// src/main/java/com/example/microusers/service/UserService.java
package com.example.microusers.service;

import com.example.microusers.model.User;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class UserService {

    private List<User> users = new ArrayList<>();
    private AtomicLong counter = new AtomicLong(1);

    public UserService() {
        // Agregar usuarios predeterminados
        users.add(new User(counter.getAndIncrement(), "Juan Perez", "juan@example.com", "juan", "password123"));
        users.add(new User(counter.getAndIncrement(), "Maria Gomez", "maria@example.com", "maria", "password456"));
    }

    @Bulkhead(name = "userService", type = Bulkhead.Type.SEMAPHORE)
    public User authenticate(String username, String password) {
        return users.stream()
                .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }

    @Bulkhead(name = "userService", type = Bulkhead.Type.SEMAPHORE)
    public List<User> getAllUsers() {
        return users;
    }

    @Bulkhead(name = "userService", type = Bulkhead.Type.SEMAPHORE)
    public User createUser(User user) {
        user.setId(counter.getAndIncrement());
        users.add(user);
        return user;
    }

    @Bulkhead(name = "userService", type = Bulkhead.Type.SEMAPHORE)
    public User updateUser(Long id, User updatedUser) {
        User user = getUserById(id);
        if (user != null) {
            user.setName(updatedUser.getName());
            user.setEmail(updatedUser.getEmail());
            user.setUsername(updatedUser.getUsername());
            user.setPassword(updatedUser.getPassword());
            return user;
        }
        return null;
    }

    @Bulkhead(name = "userService", type = Bulkhead.Type.SEMAPHORE)
    public boolean deleteUser(Long id) {
        return users.removeIf(u -> u.getId().equals(id));
    }

    private User getUserById(Long id) {
        return users.stream().filter(u -> u.getId().equals(id)).findFirst().orElse(null);
    }
}
