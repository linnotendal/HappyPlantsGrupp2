package com.happyplants2.plantapp.controller;

import com.happyplants2.plantapp.service.PlantImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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