package com.happyplants2.plantapp.service;

import com.happyplants2.plantapp.model.User;
import com.happyplants2.plantapp.repository.UserPlantsRepository;
import com.happyplants2.plantapp.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserPlantsRepository userPlantsRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(String email, String username, String password) {
        if (email==null || email.isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (username==null || username.isBlank()) {
            throw new IllegalArgumentException("Username is required");
        }
        if (password==null || password.isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }
        if(userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists");
        }
        String hashedPassword= passwordEncoder.encode(password);
        User user= new User(email, username, hashedPassword);
        userRepository.save(user);
        return user;
    }

    public User loginUser(String email, String password) {
        User user = userRepository.findByEmail(email);
        if(user == null) {
            throw new IllegalArgumentException("User with this email is not found");
        }
        if(!passwordEncoder.matches(password,user.getPassword())) {
            throw new IllegalArgumentException("Wrong password");
        }
        return user;
    }

    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email));
    }

    public boolean deleteUser(long userId) {
        if(!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User with this id is not found");
        }
        userPlantsRepository.deleteByUserId(userId);
        userRepository.deleteById(userId);
        return true;
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public void logOutUser(HttpSession session) {
        session.invalidate();
    }
}
