package com.happyplants2.plantapp.service;


import com.happyplants2.plantapp.DTO.UserPlantsResponseDTO;
import com.happyplants2.plantapp.model.PlantTemplate;
import com.happyplants2.plantapp.model.User;
import com.happyplants2.plantapp.model.UserPlant;
import com.happyplants2.plantapp.repository.PlantTemplateRepository;
import com.happyplants2.plantapp.repository.UserPlantsRepository;
import com.happyplants2.plantapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class will handle user library services like adding, removing, searching filtering
 * and so on
 */

@Service
public class LibraryService {
    @Autowired
    private PlantTemplateRepository plantTemplateRepository;
    @Autowired
    private UserPlantsRepository userPlantsRepository;
    @Autowired
    private UserRepository userRepository;

    public List<UserPlantsResponseDTO> getUserPlants(Long userId) {
        List<UserPlant> userPlants= userPlantsRepository.findByUser_Id(userId);
        return userPlants.stream()
                .map(UserPlantsResponseDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Waters a plant in a user repository
     * @param userPlantId
     * @return
     */
    public UserPlant waterPlant(Long userPlantId) {
        UserPlant userPlant = userPlantsRepository.findById(userPlantId)
                .orElseThrow(() -> new IllegalArgumentException("User plant not found"));
        userPlant.setLastWatered(LocalDate.now());
        return userPlantsRepository.save(userPlant);
    }

    /**
     * This method is used to add a plant to a specific users library
     * @param userId
     * @param plantTemplateId
     * @return
     */
    public UserPlant addPlantToUser(Long userId, Integer plantTemplateId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        PlantTemplate plantTemplate = plantTemplateRepository.findById(plantTemplateId)
                .orElseThrow(() -> new IllegalArgumentException("Plant not found"));
        UserPlant userPlant = new UserPlant();
        userPlant.setUser(user);
        userPlant.setPlant(plantTemplate);
        userPlant.setLastWatered(LocalDate.now());

        return userPlantsRepository.save(userPlant);
    }

    /**
     * Removes a plant from users library
     * @param userPlantId
     */
    public void removePlant(Long userPlantId) {
        userPlantsRepository.deleteById(userPlantId);
    }
}
