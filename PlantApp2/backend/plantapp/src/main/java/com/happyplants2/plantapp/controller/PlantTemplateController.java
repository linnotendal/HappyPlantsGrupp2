package com.happyplants2.plantapp.controller;

import com.happyplants2.plantapp.DTO.PlantDto;
import com.happyplants2.plantapp.DTO.PlantResponseDTO;
import com.happyplants2.plantapp.service.DiscoverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Responsible for Discover library
 */
@RestController
public class PlantTemplateController {
    @Autowired
    private DiscoverService service;
    @GetMapping("/templates/search")
    public List<PlantResponseDTO> searchTemplates(@RequestParam String name) {
        return service.search(name);
    }
}
