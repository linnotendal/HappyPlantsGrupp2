package com.happyplants2.plantapp.controller;


import com.happyplants2.plantapp.model.User;
import com.happyplants2.plantapp.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")

public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User loginRequest, HttpSession session) {
        try {
            User loggeduser= userService.loginUser(loginRequest.getEmail(), loginRequest.getPassword());
            session.setAttribute("userId", loggeduser.getId());
            return ResponseEntity.ok("Login successful");
        }catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            User registeredUser= userService.registerUser(user.getEmail(), user.getUsername(), user.getPassword());
            return ResponseEntity.ok("User registered successfully");
        }catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
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
       try{ userService.logOutUser(session);
        return ResponseEntity.ok("User logout successfully");
       }catch (IllegalArgumentException e) {
           return ResponseEntity.badRequest().body(e.getMessage());
       }

    }
}
