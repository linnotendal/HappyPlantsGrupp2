package com.happyplants2.plantapp.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SpeciesListDTO {
    private List<PlantDto> data;
    public List<PlantDto> getData() {
        return data;
    }
}
