package com.happyplants2.plantapp.service;

import com.happyplants2.plantapp.DTO.PlantResponseDTO;
import com.happyplants2.plantapp.model.PlantTemplate;
import com.happyplants2.plantapp.repository.PlantTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * This Class is Responsible to Handle Plant search in Discover section
 */
@Service
public class DiscoverService {
    @Autowired
    private PlantTemplateRepository plantTemplateRepository;

    public List<PlantResponseDTO> search (String plantName) {
        if (plantName == null || plantName.isEmpty()) {
            return Collections.emptyList();
        }
        return plantTemplateRepository.
                findByCommonNameContainingIgnoreCase(plantName).stream().map(this::convertToDto).toList();
    }
    private PlantResponseDTO convertToDto (PlantTemplate plantTemplate) {
        String careLevel=calculateCareLevel(plantTemplate.getWaterFrequencyDays(),plantTemplate.getSunlight());

        return new PlantResponseDTO(
                plantTemplate.getId(),
                plantTemplate.getCommonName(),
                plantTemplate.getScientificName(),
                plantTemplate.getFamily(),
                plantTemplate.getWatering(),
                plantTemplate.getSunlight(),
                plantTemplate.getImageUrl(),
                plantTemplate.getWaterFrequencyDays(),
                careLevel);
    }
    private String calculateCareLevel(Integer waterFrequencyDays, String sunlight){
        if (waterFrequencyDays==null){
            return "Unknown";
        }
        String sun;
        if (sunlight==null){
            sun="";
        } else {
            sun=sunlight.toLowerCase();
        }
        boolean highSun= sun.contains("full") || sun.contains("direct") || sun.contains("bright");
        boolean lowSun  = sun.contains("low") || sun.contains("shade") || sun.contains("indirect");

        if (waterFrequencyDays <=5||highSun)return "hard";
        if (waterFrequencyDays<=9) return "Medium";
        if (waterFrequencyDays>= 10 || lowSun)return "Easi";
        return "Medium";
    }
    public List<PlantResponseDTO> getAllPlants() {
        return plantTemplateRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .toList();
    }
    public PlantResponseDTO getPlantById(Integer id) {
        PlantTemplate plantTemplate = plantTemplateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plant not found with id: " + id));

        return convertToDto(plantTemplate);
    }
}
