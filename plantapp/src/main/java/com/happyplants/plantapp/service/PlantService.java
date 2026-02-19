package com.happyplants.plantapp.service;

import com.happyplants.plantapp.entity.Plant;
import com.happyplants.plantapp.repository.PlantRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlantService {

    private final PlantRepo plantRepo;

    public PlantService(PlantRepo plantRepo) {
        this.plantRepo = plantRepo;
    }

    public List<Plant> getAllPlants() {
        return plantRepo.findAll();
    }

    public Plant getPlantById(Long id) {
        return plantRepo.findById(id).orElseThrow(() -> new RuntimeException("Plant not found"));
    }

    public Plant savePlant(Plant plant) {
        return plantRepo.save(plant);
    }

    public void deletePlant(Long id) {
        plantRepo.deleteById(id);
    }
}
