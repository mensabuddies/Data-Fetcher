package com.example.mensaapi.database.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.net.URL;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "canteens")
@NoArgsConstructor
@Getter
@Setter
public class Canteen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "canteen_id", unique = true)
    private Integer id;

    @Column(unique = true)
    private String name;

    @ManyToOne(optional = false)
    @JoinColumn(name = "location_id", nullable = false)
    @JsonManagedReference
    private Location location;

    @OneToMany(mappedBy = "canteen", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<OpeningHours> openingHours;

    private String info;

    @Column(name = "additional_info")
    private String additionalInfo;

    @Column(name = "link_to_food_plan")
    private String linkToFoodPlan;

    @OneToMany(mappedBy = "canteen", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<Menu> menus;
}