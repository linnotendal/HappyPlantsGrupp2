package com.happyplants2.plantapp.repository;

import com.happyplants2.plantapp.model.UserPlant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserPlantsRepository extends JpaRepository<UserPlant, Long> {
    List<UserPlant> findByUser_Id(Long userId);
}
