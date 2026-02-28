package com.happyplants2.plantapp.model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PlantTest {

    @Test
    void progressNeverBelowMinimum() {
        PlantTemplate template = new PlantTemplate();
        template.setWaterFrequencyDays(5);

        User user = new User("a@a.com", "a", "p");
        UserPlant plant = new UserPlant(
                user,
                template,
                LocalDate.now().minusDays(100)
        );

        assertEquals(0.02, plant.getProgress());
    }

    @Test
    void progressReturnsFullWhenRecentlyWatered() {
        PlantTemplate template = new PlantTemplate();
        template.setWaterFrequencyDays(5);

        UserPlant plant = new UserPlant(
                new User("a@a.com", "a", "p"),
                template,
                LocalDate.now()
        );

        assertEquals(1.0, plant.getProgress());
    }

    @Test
    void progressAtMidPoint() {
        PlantTemplate template = new PlantTemplate();
        template.setWaterFrequencyDays(6);

        UserPlant plant = new UserPlant(
                new User("a@a.com", "a", "p"),
                template,
                LocalDate.now().minusDays(3)
        );

        assertEquals(0.5, plant.getProgress(), 0.001);
    }

    @Test
    void plantsNeedsWateringWhenDaysPassed() {
        PlantTemplate template = new PlantTemplate();
        template.setWaterFrequencyDays(5);

        UserPlant plant = new UserPlant(
                new User("a@a.com", "a", "p"),
                template,
                LocalDate.now().minusDays(5)
        );

        assertTrue(plant.getDaysUntilWater() <= 0);
    }

    @Test
    void wateringUpdatesLastWateredDay() {
        PlantTemplate template = new PlantTemplate();
        template.setWaterFrequencyDays(5);

        UserPlant plant = new UserPlant(
                new User("a@a.com", "a", "p"),
                template,
                LocalDate.now().minusDays(5)
        );

        plant.water();
        assertEquals(LocalDate.now(), plant.getLastWatered());
    }
}