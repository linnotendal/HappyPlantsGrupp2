package com.happyplants2.plantapp.controller;

import com.happyplants2.plantapp.model.Plant;
import com.happyplants2.plantapp.repository.PlantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/plants")
@CrossOrigin(origins = "*")
public class PlantController {

    @Autowired
    private PlantRepository plantRepository;

    @GetMapping
    public List<Plant> getAllPlants() {
        return plantRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Plant> getPlantById(@PathVariable Long id) {
        return plantRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Plant createPlant(@RequestBody Plant plant) {
        plant.setLastWatered(LocalDate.now());
        return plantRepository.save(plant);
    }

    @PutMapping("/{id}/water")
    public ResponseEntity<Plant> waterPlant(@PathVariable Long id) {
        return plantRepository.findById(id)
                .map(plant -> {
                    plant.water();
                    Plant updated = plantRepository.save(plant);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlant(@PathVariable Long id) {
        if (plantRepository.existsById(id)) {
            plantRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
