package com.happyplants2.plantapp.service;

import com.happyplants2.plantapp.DTO.SpeciesListDTO;
import com.happyplants2.plantapp.model.PlantTemplate;
import com.happyplants2.plantapp.repository.PlantTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * This class will handle getting information from the API and saving plant information to the database
 *
 */
@Service
public class PlantImportService {
    @Value("${perenual.api.key}")
    private String apiKey;

    @Autowired
    private PlantTemplateRepository repository;
    @Autowired
    private RestTemplate restTemplate;

    public void importIndoorPlants(int limit) {
        String url = "https://perenual.com/api/species-list?key=" + apiKey + "&per_page=" + limit + "&indoor=true";
        SpeciesListDTO response = restTemplate.getForObject(url, SpeciesListDTO.class);

        if (response == null || response.getData() == null) return;

        for (SpeciesListDTO.PlantData dto : response.getData()) {
            PlantTemplate plant = new PlantTemplate(dto.getId(),dto.getCommon_name(), dto.getScientific_name()!= null ? String.join(", ", dto.getScientific_name()) : null,
                    dto.getFamily(),
                    dto.getWatering(),
                    dto.getSunlight() != null ? String.join(", ", dto.getSunlight()) : null,
                    dto.getDefault_image() != null ? dto.getDefault_image().getOriginal_url() : null
            );
            repository.save(plant);
        }
    }
}