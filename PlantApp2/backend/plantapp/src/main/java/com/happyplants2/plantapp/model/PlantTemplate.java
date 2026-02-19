package com.happyplants2.plantapp.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.criteria.CriteriaBuilder;

@Entity
@Table(name ="plants_general")
public class PlantTemplate {
    @Id
    private Integer id;
    private String commonName;

    @Column(length = 100)
    private String scientificName;

    private String watering;

    @Column(nullable = false)
    private Integer waterFrequencyDays;

    private String family;

    @Column(length = 1000)
    private String sunlight;

    private String imageUrl;

    public PlantTemplate() {}

    public PlantTemplate(int id, String commonName, String scientificName, String family,
                         String watering, String sunlight, String imageUrl, Integer waterFrequencyDays) {
        this.id = id;
        this.commonName = commonName;
        this.scientificName = scientificName;
        this.family = family;
        this.watering = watering;
        this.sunlight = sunlight;
        this.imageUrl = imageUrl;
        this.waterFrequencyDays = waterFrequencyDays;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getCommonName() { return commonName; }
    public void setCommonName(String commonName) { this.commonName = commonName; }

    public String getScientificName() { return scientificName; }
    public void setScientificName(String scientificName) { this.scientificName = scientificName; }

    public String getWatering() { return watering; }
    public void setWatering(String watering) { this.watering = watering; }

    public String getSunlight() { return sunlight; }
    public void setSunlight(String sunlight) { this.sunlight = sunlight; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public Integer getWaterFrequencyDays() {
        return waterFrequencyDays;
    }

    public void setWaterFrequencyDays(Integer waterFrequencyDays) {
        this.waterFrequencyDays = waterFrequencyDays;
    }
}
