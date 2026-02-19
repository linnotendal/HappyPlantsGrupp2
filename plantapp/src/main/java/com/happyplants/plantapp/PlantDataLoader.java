package com.happyplants.plantapp;

import com.happyplants.plantapp.service.PlantFetchService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class PlantDataLoader implements CommandLineRunner {

    private final PlantFetchService fetchService;

    public PlantDataLoader(PlantFetchService fetchService) {
        this.fetchService = fetchService;
    }

    @Override
    public void run(String... args) throws Exception {
        //fetchService.fetchAndSavePlants(1);
        System.out.println("Finished fetching plants!");
    }
}