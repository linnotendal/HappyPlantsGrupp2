package com.happyplants2.plantapp.controller;

import com.happyplants2.plantapp.DTO.PlantResponseDTO;
import com.happyplants2.plantapp.service.DiscoverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Responsible for Discover library
 * Gets All plants stored in the database and returns them to the frontend
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class PlantTemplateController {
    @Autowired
    private DiscoverService service;

    @GetMapping("/discover/search")
    public List<PlantResponseDTO> searchPlants(@RequestParam String name) {
        return service.search(name);
    }
    @GetMapping("discover")
    public List<PlantResponseDTO> getPlants(@RequestParam(required = false) String name) {
        if (name == null || name.isEmpty()) {
            return service.getAllPlants();
        }
        return service.search(name);
    }
}
