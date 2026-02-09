package com.happyplants2.plantapp.service;

import com.happyplants2.plantapp.model.User;
import com.happyplants2.plantapp.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User registerUser(String email, String username, String password) {
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if(userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists");
        }
        User user= new User(email, username, password);
        userRepository.save(user);
        return user;
    }

    public User loginUser(String email, String password) {
        User user = userRepository.findByEmail(email);
        if(user == null) {
            throw new IllegalArgumentException("User with this email is not found");
        }
        if(!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("Wrong password");
        }
        return user;
    }

    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email));
    }

    public void deleteUser(long userId) {
        userRepository.deleteById(userId);
    }

    public void logOutUser(HttpSession session) {
        session.invalidate();
    }
}
