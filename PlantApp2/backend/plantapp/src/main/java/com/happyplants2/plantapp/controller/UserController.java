package com.happyplants2.plantapp.controller;


import com.happyplants2.plantapp.DTO.UserPlantsResponseDTO;
import com.happyplants2.plantapp.model.User;
import com.happyplants2.plantapp.service.LibraryService;
import com.happyplants2.plantapp.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * This Class is responsible for handling user logic like register, log in , log out ..
 */
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:63342", allowCredentials = "true")

public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private LibraryService libraryService;
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User loginRequest, HttpSession session) {
        try {
            User loggedUser= userService.loginUser(loginRequest.getEmail(), loginRequest.getPassword());
            session.setAttribute("userId", loggedUser.getId());
            List<UserPlantsResponseDTO> plants = libraryService.getUserPlants(loggedUser.getId());

            return ResponseEntity.ok(Map.of(
                    "user", loggedUser,
                    "plants", plants
            ));
        }catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            userService.registerUser(user.getEmail(), user.getUsername(), user.getPassword());
            return ResponseEntity.ok("User registered successfully");
        }catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        System.out.println("me was called");
        if (userId == null) {
            return ResponseEntity.status(401).body("Not logged in");
        }
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(user);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") long userId) {
       try {
           userService.deleteUser(userId);
           return ResponseEntity.ok("User deleted successfully");
       }catch (IllegalArgumentException e) {
           return ResponseEntity.badRequest().body(e.getMessage());
       }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpSession session) {
        System.out.println("log out was called");
       try{ userService.logOutUser(session);
        return ResponseEntity.ok("User logout successfully");
       }catch (IllegalArgumentException e) {
           return ResponseEntity.badRequest().body(e.getMessage());
       }
    }
}
