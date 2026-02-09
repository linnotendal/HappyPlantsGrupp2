package com.happyplants2.plantapp.controller;


import com.happyplants2.plantapp.model.User;
import com.happyplants2.plantapp.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")

public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/user/login")
    public ResponseEntity<User> loginUser(@RequestBody User loginRequest, HttpSession session) {
        try {
            User user= userService.loginUser(loginRequest.getEmail(), loginRequest.getPassword());
            session.setAttribute("user", user);
            return ResponseEntity.ok(user);
        }catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    @PostMapping("/user/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        try {
            User registeredUser= new User(user.getEmail(), user.getUsername(), user.getPassword());
            return ResponseEntity.ok(registeredUser);
        }catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
