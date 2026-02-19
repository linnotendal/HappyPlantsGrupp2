package com.happyplants2.plantapp.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * There is a possibility that this class will be removed later
 * and replaced with userPlants and plantsTemplates
 */
@Entity
@Table(name = "plants")
public class Plant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nickname;
    private String plantId;
    private LocalDate lastWatered;
    private int waterFrequencyDays;
    private String imageURL;

    public Plant() {}

    public Plant(String nickname, String plantId, LocalDate lastWatered, int waterFrequencyDays, String imageURL) {
        this.nickname = nickname;
        this.plantId = plantId;
        this.lastWatered = lastWatered;
        this.waterFrequencyDays = waterFrequencyDays;
        this.imageURL = imageURL;
    }

    // logic from old code
    public double getProgress() {
        long daysSinceWatered = ChronoUnit.DAYS.between(lastWatered, LocalDate.now());
        double progress = 1.0 - ((double) daysSinceWatered / (double) waterFrequencyDays);

        if (progress <= 0.02) return 0.02;
        else if (progress >= 0.95) return 1.0;
        return progress;
    }

    public int getDaysUntilWater() {
        long daysSinceWatered = ChronoUnit.DAYS.between(lastWatered, LocalDate.now());
        return (int) (waterFrequencyDays - daysSinceWatered);
    }

    public boolean needsWatering() {
        return getDaysUntilWater() <= 0;
    }

    public void water() {
        this.lastWatered = LocalDate.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public String getPlantId() { return plantId; }
    public void setPlantId(String plantId) { this.plantId = plantId; }

    public LocalDate getLastWatered() { return lastWatered; }
    public void setLastWatered(LocalDate lastWatered) { this.lastWatered = lastWatered; }

    public int getWaterFrequencyDays() { return waterFrequencyDays; }
    public void setWaterFrequencyDays(int waterFrequencyDays) { this.waterFrequencyDays = waterFrequencyDays; }

    public String getImageURL() { return imageURL; }
    public void setImageURL(String imageURL) { this.imageURL = imageURL; }
}