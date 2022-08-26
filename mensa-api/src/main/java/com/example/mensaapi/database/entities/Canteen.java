package com.example.mensaapi.database.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.net.URL;
import java.util.Set;

@Data
@Entity
@Table(name = "canteens")
@NoArgsConstructor
public class Canteen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "canteen_id")
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @OneToMany(mappedBy = "canteen", cascade = CascadeType.ALL)
    private Set<OpeningHours> openingHours;

    private String info;
    @Column(name = "additional_info")
    private String additionalInfo;
    @Column(name = "link_to_food_plan")
    private URL linkToFoodPlan;

}