package com.happyplants2.plantapp.service;

import com.happyplants2.plantapp.DTO.PlantDto;
import com.happyplants2.plantapp.DTO.SpeciesListDTO;
import com.happyplants2.plantapp.model.PlantTemplate;
import com.happyplants2.plantapp.repository.PlantTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class will handle getting information from the external API and saving plant information to the database
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

    /**
     * Calls The API to fill the database with general information
     * If the database has data already, The API will not be called
     */
    @EventListener(ApplicationReadyEvent.class)
    public void runImportOnStartup() {
        if (repository.count() > 10) {
            System.out.println("API will not be called, the database has data");
            fixFamilyNames();
            return;
        }
        System.out.println("application started, loading plants from API has started");
        importIndoorPlants(10);
    }

    /**
     * This method checks for each scientific name and adds the first part as the family. This is not technically
     * as this is just a species, not family. But it fills family..
     */
    private void fixFamilyNames() {
        List<PlantTemplate> plants = repository.findAll();

        for (PlantTemplate plant : plants) {
            String family = plant.getFamily();
            String scientificName = plant.getScientificName();

            if ((family == null || family.isBlank()) &&
                    scientificName != null && !scientificName.isBlank()) {

                String firstWord = scientificName.trim().split("\\s+")[0];
                plant.setFamily(firstWord);
            }
        }

        repository.saveAll(plants);
    }

    public void importIndoorPlants(int pages) {
        List<PlantTemplate> plants = new ArrayList<>();

        try {
            for (int page = 1; page <= pages; page++) {

                String url = "https://perenual.com/api/species-list?key="
                        + apiKey
                        + "&per_page=100"
                        + "&page=" + page
                        + "&indoor=true";

                SpeciesListDTO response = restTemplate.getForObject(url, SpeciesListDTO.class);

                if (response == null || response.getData() == null) continue;

                for (PlantDto dto : response.getData()) {
                    PlantTemplate plant = new PlantTemplate();

                    plant.setId(dto.getId());
                    plant.setCommonName(dto.getCommonName());

                    if (dto.getScientificName() != null && !dto.getScientificName().isEmpty()) {
                        plant.setScientificName(String.join(", ", dto.getScientificName()));
                    }

                    plant.setFamily(dto.getFamily());

                    if (dto.getSunlight() != null && !dto.getSunlight().isEmpty()) {
                        plant.setSunlight(String.join(", ", dto.getSunlight()));
                    }

                    if (dto.getDefaultImage() != null) {
                        plant.setImageUrl(dto.getDefaultImage().getOriginal_url());
                    }

                    Integer waterDays = extractWateringDays(dto);
                    if (waterDays == null) {
                        waterDays = 7;
                    }

                    plant.setWaterFrequencyDays(waterDays);

                    plants.add(plant);
                }
            }

            repository.saveAll(plants);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method will get details about a specific plant from the API
     * @param id
     * @return
     */
    public PlantDto getPlantDetails(Integer id) {
        String detailUrl = "https://perenual.com/api/species/details/"
                + id + "?key=" + apiKey;
        String rawJson = restTemplate.getForObject(detailUrl, String.class);
        System.out.println("Raw JSON for ID " + id + ": " + rawJson);
        System.out.println("Fetching details from: " + detailUrl);
        return restTemplate.getForObject(detailUrl, PlantDto.class);
    }

    public void importSinglePlant(Integer id){
        try{PlantDto plantDto = getPlantDetails(id);
        if(plantDto != null){
            PlantTemplate plantTemplate= new PlantTemplate();
            plantTemplate.setId(plantDto.getId());
            plantTemplate.setCommonName(plantDto.getCommonName());
            plantTemplate.setFamily(plantDto.getFamily());
            plantTemplate.setWatering(plantDto.getWatering());
            if (plantDto.getScientificName() != null && !plantDto.getScientificName().isEmpty()) {
                plantTemplate.setScientificName(String.join(", ", plantDto.getScientificName()));
            }

            if (plantDto.getSunlight() != null && !plantDto.getSunlight().isEmpty()) {
                plantTemplate.setSunlight(String.join(", ", plantDto.getSunlight()));
            }

            if (plantDto.getDefaultImage() != null) {
                plantTemplate.setImageUrl(plantDto.getDefaultImage().getOriginal_url());
            }
            if (plantDto.getSunlight() != null && !plantDto.getSunlight().isEmpty()) {
                plantTemplate.setSunlight(String.join(", ", plantTemplate.getSunlight()));
            }
            Integer waterDays = extractWateringDays(plantDto);
            plantTemplate.setWaterFrequencyDays(waterDays != null ? waterDays : 7);
            repository.save(plantTemplate);
            System.out.println("Successfully saved plant: " + plantDto.getCommonName());

        }}catch (Exception e){
            System.err.println("Error importing plant ID " + id + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
    private Integer extractWateringDays(PlantDto details) {
        if (details.getWateringGeneralBenchmark() == null)
            return null;
        String value= details.getWateringGeneralBenchmark().getValue();
        String unit= details.getWateringGeneralBenchmark().getUnit();
        if(value==null || unit==null)
            return null;
        value = value.replace("\"", "").trim();
        if (!unit.equalsIgnoreCase("days"))
            return null;
        try {
            if (value.contains("-")) {
                String[] parts = value.split("-");
                return Integer.parseInt(parts[0].trim());
            }
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}