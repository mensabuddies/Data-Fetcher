package com.example.mensaapi.database.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name = "locations")
@NoArgsConstructor
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id")
    private Integer id;

    @Column(unique = true)
    private String location;

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL)
    private Set<Canteen> canteens;
}