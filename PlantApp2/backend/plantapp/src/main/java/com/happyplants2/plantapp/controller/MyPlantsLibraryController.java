package com.happyplants2.plantapp.controller;

import com.happyplants2.plantapp.DTO.UserPlantsResponseDTO;
import com.happyplants2.plantapp.model.UserPlant;
import com.happyplants2.plantapp.service.LibraryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/user-plants")
public class MyPlantsLibraryController {
    @Autowired
    private LibraryService myPlantsLibraryService;

    // tested
    @GetMapping
    public ResponseEntity<?> getUserPlants(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if(userId == null) {
            return ResponseEntity.status(401).body("User is not logged in");
        }
        return ResponseEntity.ok(myPlantsLibraryService.getUserPlants(userId));
    }
    // tested
    @PostMapping("/add/{plantId}")
    public ResponseEntity<?> addPlant(@PathVariable Integer plantId, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if(userId == null) {
            return ResponseEntity.status(401).body("User is not logged in");
        }
        return ResponseEntity.ok(myPlantsLibraryService.addPlantToUser(userId, plantId));
    }
    //Tested
    @PutMapping("/water/{userPlantId}")
    public ResponseEntity<?> waterPlant(@PathVariable Long userPlantId, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if(userId == null) {
            return ResponseEntity.status(401).body("User is not logged in");
        }
        return ResponseEntity.ok(myPlantsLibraryService.waterPlant(userPlantId));
    }

    @DeleteMapping("/remove/{userPlantId}")
    public ResponseEntity<Void> deletePlant(@PathVariable Long userPlantId) {
        myPlantsLibraryService.removePlant(userPlantId);
        return ResponseEntity.ok().build();
    }
}
