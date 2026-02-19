package com.happyplants2.plantapp.controller;

import com.happyplants2.plantapp.model.Plant;
import com.happyplants2.plantapp.repository.PlantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class PlantController {

    @Autowired
    private PlantRepository plantRepository;

    /**
     * Get all plants in user's library
     * GET /api/library
     */
    @GetMapping("/library")
    public List<Plant> getLibrary() {
        return plantRepository.findAll()
                .stream()
                .sorted(Comparator.comparingInt(Plant::getDaysUntilWater)) //Returns plants based on days until water.
                .toList();
    }

    /**
     * Add a plant to user's library
     * POST /api/library
     */
    @PostMapping("/library")
    public Plant addToLibrary(@RequestBody Plant plant) {
        if (plant.getLastWatered() == null) {
            plant.setLastWatered(LocalDate.now());
        }
        return plantRepository.save(plant);
    }

    /**
     * Remove a plant from library
     * DELETE /api/library/{id}
     */
    @DeleteMapping("/library/{id}")
    public ResponseEntity<Void> removeFromLibrary(@PathVariable Long id) {
        if (plantRepository.existsById(id)) {
            plantRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Water a plant (update lastWatered to today)
     * PUT /api/library/{id}/water
     */
    @PutMapping("/library/{id}/water")
    public ResponseEntity<Plant> waterPlant(@PathVariable Long id) {
        return plantRepository.findById(id)
                .map(plant -> {
                    plant.water();
                    Plant updated = plantRepository.save(plant);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get a specific plant
     * GET /api/library/{id}
     */
    @GetMapping("/library/{id}")
    public ResponseEntity<Plant> getPlantById(@PathVariable Long id) {
        return plantRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}