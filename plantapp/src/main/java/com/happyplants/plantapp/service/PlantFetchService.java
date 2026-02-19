package com.happyplants.plantapp.service;

import com.happyplants.plantapp.dto.ApiResponseWrapper;
import com.happyplants.plantapp.dto.PlantApiDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.happyplants.plantapp.entity.Plant;
import com.happyplants.plantapp.repository.PlantRepo;

@Service
public class PlantFetchService {

    private final PlantRepo plantRepo;
    private final RestTemplate restTemplate;

    private final String API_KEY = "";

    public PlantFetchService(PlantRepo plantRepo) {
        this.plantRepo = plantRepo;
        this.restTemplate = new RestTemplate();
    }

    public void fetchAndSavePlants(int page) {
        String url = "https://perenual.com/api/v2/species-list?key=" + API_KEY + "&page=" + page;

        ApiResponseWrapper response = restTemplate.getForObject(url, ApiResponseWrapper.class);

        if (response != null && response.getData() != null) {
            for (PlantApiDto dto : response.getData()) {
                // Check if plant already exists
                if (!plantRepo.existsById(dto.getId())) {
                    Plant plant = new Plant();
                    plant.setId(dto.getId());
                    plant.setCommonName(dto.getCommon_name());
                    plant.setScientificName(String.join(", ", dto.getScientific_name()));
                    if (dto.getDefaultImage() != null) {
                        plant.setImage(dto.getDefaultImage().getMedium_url());
                    }

                    plantRepo.save(plant);
                }
            }
        }
    }
}