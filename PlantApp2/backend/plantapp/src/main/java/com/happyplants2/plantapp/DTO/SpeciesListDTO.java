package com.happyplants2.plantapp.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SpeciesListDTO {
    private List<PlantData> data;
    public List<PlantData> getData() {
        return data;
    }

    public static class PlantData {
        private Integer id;
        private String common_name;
        private List<String> scientific_name;
        private String watering;
        private List<String> sunlight;
        private DefaultImage default_image;

        public String getFamily() {
            return family;
        }

        public void setFamily(String family) {
            this.family = family;
        }

        private String family;


        public List<String> getScientific_name() {
            return scientific_name;
        }

        public void setScientific_name(List<String> scientific_name) {
            this.scientific_name = scientific_name;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getCommon_name() {
            return common_name;
        }

        public void setCommon_name(String common_name) {
            this.common_name = common_name;
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

        public DefaultImage getDefault_image() {
            return default_image;
        }

        public void setDefault_image(DefaultImage default_image) {
            this.default_image = default_image;
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
}
