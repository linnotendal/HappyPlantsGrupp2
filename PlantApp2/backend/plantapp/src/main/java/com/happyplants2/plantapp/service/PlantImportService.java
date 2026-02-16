package com.happyplants2.plantapp.service;

import com.happyplants2.plantapp.DTO.PlantDto;
import com.happyplants2.plantapp.DTO.SpeciesListDTO;
import com.happyplants2.plantapp.model.PlantTemplate;
import com.happyplants2.plantapp.repository.PlantTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

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
        try{
            SpeciesListDTO response = restTemplate.getForObject(url, SpeciesListDTO.class);
            if (response == null || response.getData() == null) return;
            List<PlantTemplate> plants = new ArrayList<>();

            for (PlantDto dto : response.getData()) {
                //if(repository.existsById(dto.getId())) continue;

                PlantDto details = getPlantDetails(dto.getId());
                if (details!=null) {
                    System.out.println("Successfully fetched details for: " + details.getCommonName());
                    System.out.println("Sunlight data: " + details.getSunlight());
                    Integer waterDays = extractWateringDays(details);
                    PlantTemplate plant = new PlantTemplate();
                    plant.setId(details.getId());
                    plant.setCommonName(details.getCommonName());
                    if (details.getScientificName() != null) {
                        plant.setScientificName(String.join(", ", details.getScientificName()));
                    }
                    plant.setFamily(details.getFamily());
                    plant.setWatering(details.getWatering());
                    if (details.getSunlight() != null) {
                        plant.setSunlight(String.join(", ", details.getSunlight()));
                    }
                    if (details.getDefaultImage() != null) {
                        plant.setImageUrl(details.getDefaultImage().getOriginal_url());
                    }

                    String sunlightString = "";
                    if (details.getSunlight() != null && !details.getSunlight().isEmpty()) {
                            sunlightString = String.join(", ", details.getSunlight());
                        }
                    plant.setSunlight(sunlightString);
                    System.out.println("Plant ID: " + details.getId() + " Sunlight: " + details.getSunlight());
                    plant.setWaterFrequencyDays(waterDays);

                    plants.add(plant);
                }
               /** PlantTemplate plant = new PlantTemplate(details.getId(),details.getCommonName(), details.getScientificName()!= null ? String.join(", ", details.getScientificName()) : null,
                        details.getFamily(),
                        details.getWatering(),
                        details.getSunlight() != null ? String.join(", ", details.getSunlight()) : null,
                        details.getDefaultImage() != null ? details.getDefaultImage().getOriginal_url() : null,
                        waterDays
                );*/
                }
            repository.saveAll(plants);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private PlantDto getPlantDetails(Integer id) {
        String detailUrl = "https://perenual.com/api/species/details/"
                + id + "?key=" + apiKey;
        String rawJson = restTemplate.getForObject(detailUrl, String.class);
        System.out.println("Raw JSON for ID " + id + ": " + rawJson);
        System.out.println("Fetching details from: " + detailUrl);
        return restTemplate.getForObject(detailUrl, PlantDto.class);
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