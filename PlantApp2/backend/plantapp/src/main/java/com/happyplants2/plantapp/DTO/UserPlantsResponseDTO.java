package com.happyplants2.plantapp.DTO;

import com.happyplants2.plantapp.model.UserPlant;

import java.time.LocalDate;

public class UserPlantsResponseDTO {


    private Long userPlantId;
    private Integer plantId;
    private String commonName;
    private String scientificName;
    private String imageUrl;
    private LocalDate lastWatered;
    private String nickName;
    private String location;
    private Integer wateringIntervalDays;


    public UserPlantsResponseDTO(UserPlant userPlant) {
        this.userPlantId = userPlant.getId();
        this.plantId = userPlant.getPlant().getId();
        this.commonName = userPlant.getPlant().getCommonName();
        this.scientificName = userPlant.getPlant().getScientificName();
        this.imageUrl = userPlant.getPlant().getImageUrl();
        this.lastWatered = userPlant.getLastWatered();
        this.nickName = userPlant.getNickName();
        this.location = userPlant.getLocation();
        Integer frequency = userPlant.getPlant().getWaterFrequencyDays();
        this.wateringIntervalDays = (frequency != null) ? frequency : 0;
    }
    public Integer getPlantId() {
        return plantId;
    }

    public void setPlantId(Integer plantId) {
        this.plantId = plantId;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public LocalDate getLastWatered() {
        return lastWatered;
    }

    public void setLastWatered(LocalDate lastWatered) {
        this.lastWatered = lastWatered;
    }

    public String getNickName() {
        return nickName;
    }
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getScientificName() {
        return scientificName;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }
    public String getLocation() {
        return location;
    }

    public Integer getWateringIntervalDays() {
        return wateringIntervalDays;
    }
    public Long getUserPlantId() {
        return userPlantId;
    }

    public void setUserPlantId(Long userPlantId) {
        this.userPlantId = userPlantId;
    }
}
