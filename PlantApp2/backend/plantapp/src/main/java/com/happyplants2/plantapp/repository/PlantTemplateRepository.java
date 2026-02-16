package com.happyplants2.plantapp.repository;

import com.happyplants2.plantapp.model.PlantTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlantTemplateRepository extends JpaRepository<PlantTemplate, Integer> {
    List<PlantTemplate> findByCommonNameContainingIgnoreCase(String name);
    List<PlantTemplate> findByScientificNameContainingIgnoreCase(String name);
}
