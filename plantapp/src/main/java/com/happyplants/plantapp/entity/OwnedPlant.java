package com.happyplants.plantapp.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class OwnedPlant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Plant plant;

    private LocalDate dateAquired;
    private String notes;
}
