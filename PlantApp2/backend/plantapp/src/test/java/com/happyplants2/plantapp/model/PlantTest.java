package com.happyplants2.plantapp.model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PlantTest {

    @Test
    void ProgressNeverBelowMinimum() {
        Plant plant = new Plant("Rose","1", LocalDate.now().minusDays(100), 5,"img" );
        assertEquals(0.02, plant.getProgress());
    }

    @Test
    void ProgressReturnsFullWhenRecentlyWatered(){
        Plant plant = new Plant("Rose","1", LocalDate.now(), 5,"img" );
        assertEquals(1, plant.getProgress());
    }

    @Test
    void ProgressAtMidPoint(){
        Plant plant = new Plant("Rose","1", LocalDate.now(), 6,"img" );
        plant.setLastWatered(LocalDate.now().minusDays(3));

        assertEquals(0.5, plant.getProgress(), 0.001);
    }

    @Test
    void plantsNeedsWateringWhenDaysPassed() {
        Plant plant = new Plant("Rose","1", LocalDate.now().minusDays(5), 5,"img" );
        assertTrue(plant.needsWatering());
    }

    @Test
    void wateringUpdatesLastWateredDay() {
        Plant plant = new Plant("Rose","1", LocalDate.now().minusDays(5), 5,"img" );
        plant.water();
        assertEquals(LocalDate.now(), plant.getLastWatered());
    }
}