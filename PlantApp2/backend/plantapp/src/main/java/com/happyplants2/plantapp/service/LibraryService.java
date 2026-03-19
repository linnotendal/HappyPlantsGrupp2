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
import java.util.Random;
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
    public UserPlant addPlantToUser(Long userId, Integer plantTemplateId, String location) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        PlantTemplate plantTemplate = plantTemplateRepository.findById(plantTemplateId)
                .orElseThrow(() -> new IllegalArgumentException("Plant not found"));
        UserPlant userPlant = new UserPlant();
        userPlant.setUser(user);
        userPlant.setPlant(plantTemplate);
        userPlant.setLocation(location);
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

    /**
     * Does not in fact give the user a suggestion based on content. Instead it gives a random flower that the user
     * does not already have in the library. Since family is always null in the API we can't use this.
     * @param userId self explanatory
     * @return a random plant
     */
    public PlantTemplate getSuggestedContent(Long userId) {
        List<UserPlant> userPlants = userPlantsRepository.findByUser_Id(userId);
        List<Integer> ownedPlantIds = userPlants.stream()
                .map(up -> up.getPlant().getId())
                .toList();

        List<PlantTemplate> allPlants = plantTemplateRepository.findAll();

        List<PlantTemplate> candidates = allPlants.stream()
                .filter(p -> !ownedPlantIds.contains(p.getId()))
                .toList();

        if (candidates.isEmpty()) {
            return null;
        }

        Random random = new Random();
        return candidates.get(random.nextInt(candidates.size()));
    }

    public PlantTemplate getSuggestedPopularity(Long userId) {
        return userPlantsRepository.findMostPopularNotOwned(userId)
                .stream()
                .findFirst()
                .orElse(null);
    }

    public long countUsersWithPlant(Long plantId){
       return userPlantsRepository.countUsersWithPlant(plantId);
    }
}
