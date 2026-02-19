package com.happyplants.plantapp.repository;

import com.happyplants.plantapp.entity.Plant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlantRepo extends JpaRepository<Plant,Long> {
}
