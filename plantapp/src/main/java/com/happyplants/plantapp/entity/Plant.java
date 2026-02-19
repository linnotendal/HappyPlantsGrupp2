package com.happyplants.plantapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Plant {

    @Id
    private Long id;

    private String commonName;
    private String scientificName;
    @Column(length = 1000) // image URLs are long
    private String image;

    @OneToOne(mappedBy = "plant", cascade = CascadeType.ALL)
    private PlantDetails details;
}
