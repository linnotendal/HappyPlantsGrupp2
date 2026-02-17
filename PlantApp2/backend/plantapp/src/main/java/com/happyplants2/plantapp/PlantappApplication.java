package com.happyplants2.plantapp;

import com.happyplants2.plantapp.model.Plant;
import com.happyplants2.plantapp.repository.PlantRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@SpringBootApplication
public class PlantappApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlantappApplication.class, args);
    }

    /**
     * Temporary method, adds test plants to database on startup
     * TODO: Remove this when we have a real plant API to fetch data from
     */
    @Bean
    CommandLineRunner initTestData(PlantRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                repository.save(new Plant("Monsi", "monstera-1", LocalDate.now().minusDays(3), 7, null));
                repository.save(new Plant("Snakey", "snake-plant-1", LocalDate.now().minusDays(10), 14, null));
                repository.save(new Plant("Potty", "pothos-1", LocalDate.now().minusDays(5), 5, null));

                System.out.println("Added 3 test plants");
            }
        };
    } //TEST
}