package com.happyplants2.plantapp.controller;

import com.happyplants2.plantapp.service.PlantImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Temporary class only to test getting information from the API and saving them to the database
 * Then It will be removed and replaced with getting the information automatically when starting the application
 */

@RestController
@RequestMapping("/plants")
@CrossOrigin(origins = "*")

public class PlantImportController {
    @Autowired
    private PlantImportService importService;

    @GetMapping("/import-indoor-plants")
    public String importPlants() {
        importService.importIndoorPlants(100);
        return "Indoor plants import started!";
    }
}