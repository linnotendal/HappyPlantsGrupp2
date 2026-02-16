package com.happyplants2.plantapp.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * This Class will handle JSON object that comes from an External API
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlantDto {


    Integer id;
    @JsonProperty("common_name")
    String commonName;
    @JsonProperty("scientific_name")
    List<String> scientificName;
    String watering;
    List<String> sunlight;
    @JsonProperty("default_image")
    DefaultImage defaultImage;
    String family;
    @JsonProperty("watering_general_benchmark")
    WateringGeneralBenchmark wateringGeneralBenchmark;

    public PlantDto() {}

    public PlantDto(String commonName, List<String> scientificName, DefaultImage defaultImage,String family, String watering, List<String> sunlight, WateringGeneralBenchmark wateringGeneralBenchmark) {
        this.commonName = commonName;
        this.scientificName = scientificName;
        this.defaultImage = defaultImage;
        this.family = family;
        this.watering = watering;
        this.sunlight = sunlight;
        this.wateringGeneralBenchmark = wateringGeneralBenchmark;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public List<String> getScientificName() {
        return scientificName;
    }

    public void setScientificName(List<String> scientificName) {
        this.scientificName = scientificName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getWatering() {
        return watering;
    }

    public void setWatering(String watering) {
        this.watering = watering;
    }

    public List<String> getSunlight() {
        return sunlight;
    }

    public void setSunlight(List<String> sunlight) {
        this.sunlight = sunlight;
    }

    public DefaultImage getDefaultImage() {
        return defaultImage;
    }
    public WateringGeneralBenchmark getWateringGeneralBenchmark() {
        return wateringGeneralBenchmark;
    }

    public void setDefaultImage(DefaultImage defaultImage) {
        this.defaultImage = defaultImage;
    }
    public void setWateringGeneralBenchmark(WateringGeneralBenchmark wateringGeneralBenchmark) {
        this.wateringGeneralBenchmark = wateringGeneralBenchmark;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class WateringGeneralBenchmark{
        public void setValue(String value) {
            this.value = value;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        private String value;
        private String unit;

        public String getUnit() {
            return unit;
        }

        public String getValue() {
            return value;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DefaultImage {
        public String getOriginal_url() {
            return original_url;
        }

        public void setOriginal_url(String original_url) {
            this.original_url = original_url;
        }

        public String original_url;
    }
}
