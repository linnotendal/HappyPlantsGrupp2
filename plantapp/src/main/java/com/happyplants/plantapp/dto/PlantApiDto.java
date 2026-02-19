package com.happyplants.plantapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class PlantApiDto {

    private Long id;
    private String common_name;
    private List<String> scientific_name;

    @JsonProperty("default_image")
    private ImageWrapper defaultImage;

    @Getter
    @Setter
    public static class ImageWrapper {
        private String medium_url;
    }
}
