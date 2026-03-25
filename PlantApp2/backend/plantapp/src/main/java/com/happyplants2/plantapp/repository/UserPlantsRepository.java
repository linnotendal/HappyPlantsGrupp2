package com.happyplants2.plantapp.repository;

import com.happyplants2.plantapp.model.PlantTemplate;
import com.happyplants2.plantapp.model.UserPlant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserPlantsRepository extends JpaRepository<UserPlant, Long> {
    List<UserPlant> findByUser_Id(Long userId);

    @Query("""
    SELECT up.plant
    FROM UserPlant up
    WHERE up.plant.id NOT IN (
        SELECT up2.plant.id FROM UserPlant up2 WHERE up2.user.id = :userId
    )
    GROUP BY up.plant
    ORDER BY COUNT(up.plant) DESC
""")
    List<PlantTemplate> findMostPopularNotOwned(Long userId);

    @Query("""
    SELECT COUNT(DISTINCT up.user.id)
    FROM UserPlant up
    WHERE up.plant.id = :plantId
""")
    long countUsersWithPlant(Long plantId);
}



