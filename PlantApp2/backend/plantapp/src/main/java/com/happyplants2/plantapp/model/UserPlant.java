package com.happyplants2.plantapp.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name= "user_plants")
public class UserPlant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @ManyToOne
    @JoinColumn(name="plant_id", nullable=false)
    private PlantTemplate plant;

    @Column(name= "last_watered")
    private LocalDate lastWatered;
    private String nickName;
    private String location;


    public UserPlant() {}
    public UserPlant(User user, PlantTemplate plant, LocalDate lastWatered) {
        this.user = user;
        this.plant = plant;
        this.lastWatered = lastWatered;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
    public PlantTemplate getPlant() {
        return plant;
    }

    public void setPlant(PlantTemplate plant) {
        this.plant = plant;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getProgress() {
        if (plant==null || user==null) {
            return 0.0;
        }
        long daysSinceWatered = ChronoUnit.DAYS.between(lastWatered, LocalDate.now());
        double progress = 1.0 - ((double) daysSinceWatered / (double) plant.getWaterFrequencyDays());

        if (progress <= 0.02) return 0.02;
        else if (progress >= 0.95) return 1.0;
        return progress;
    }

    public int getDaysUntilWater() {
        long daysSinceWatered = ChronoUnit.DAYS.between(lastWatered, LocalDate.now());
        return (int) (plant.getWaterFrequencyDays() - daysSinceWatered);
    }
    public LocalDate getLastWatered() { return lastWatered; }

    public void setLastWatered(LocalDate lastWatered) { this.lastWatered = lastWatered; }

    public void water() {
        this.lastWatered = LocalDate.now();
    }
}
