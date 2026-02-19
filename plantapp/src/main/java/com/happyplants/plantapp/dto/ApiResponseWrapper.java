package com.happyplants.plantapp.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ApiResponseWrapper {

    private List<PlantApiDto> data;

}
