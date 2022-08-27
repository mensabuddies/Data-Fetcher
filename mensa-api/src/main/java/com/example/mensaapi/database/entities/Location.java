package com.example.mensaapi.database.entities;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "locations")
@NoArgsConstructor
@Getter
@Setter
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