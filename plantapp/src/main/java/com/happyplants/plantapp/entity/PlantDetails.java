package com.happyplants.plantapp.entity;

import jakarta.persistence.*;

@Entity
public class PlantDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "plant_id")
    private Plant plant;

    private Integer wateringInterval;
    private String sunExposure;
    private String prefferedSoil;


}
