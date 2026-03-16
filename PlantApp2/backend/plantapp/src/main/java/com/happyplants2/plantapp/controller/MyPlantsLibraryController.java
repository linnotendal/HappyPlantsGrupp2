package com.happyplants2.plantapp.controller;

import com.happyplants2.plantapp.DTO.UserPlantsResponseDTO;
import com.happyplants2.plantapp.model.UserPlant;
import com.happyplants2.plantapp.service.LibraryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-plants")
@CrossOrigin(origins = "http://localhost:63342", allowCredentials = "true")
public class MyPlantsLibraryController {
    @Autowired
    private LibraryService myPlantsLibraryService;

    @GetMapping
    public ResponseEntity<?> getUserPlants(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if(userId == null) {
            return ResponseEntity.status(401).body("User is not logged in");
        }
        return ResponseEntity.ok(myPlantsLibraryService.getUserPlants(userId));
    }

    @PostMapping("/add/{plantId}")
    public ResponseEntity<?> addPlant(@PathVariable Integer plantId, @RequestParam(required = false) String location,
                                      HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if(userId == null) {
            return ResponseEntity.status(401).body("User is not logged in");
        }
        UserPlant userPlant = myPlantsLibraryService.addPlantToUser(userId, plantId, location);
        return ResponseEntity.ok(userPlant);
    }

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
    @GetMapping("/suggestions/content")
    public ResponseEntity<?> getSuggestionBasedOnContent(HttpSession session){
        Long userId = (Long) session.getAttribute("userId");
        if(userId == null) {
            return ResponseEntity.status(401).body("User is not logged in");
        }

        return ResponseEntity.ok(myPlantsLibraryService.getSuggestedContent(userId));
    }

    @GetMapping("/suggestions/popular")
    public ResponseEntity<?> getSuggestionBasedOnPopularity(HttpSession session){
        Long userId = (Long) session.getAttribute("userId");
        if(userId == null) {
            return ResponseEntity.status(401).body("User is not logged in");
        }

        return ResponseEntity.ok(myPlantsLibraryService.getSuggestedPopularity(userId));
    }
}
