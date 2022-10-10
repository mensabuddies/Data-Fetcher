package com.example.mensaapi.database.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "food_providers")
@NoArgsConstructor
@Getter
@Setter
public class FoodProvider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "food_provider_id", unique = true)
    private Integer id;

    @Column(unique = true)
    private String name;

    @ManyToOne(optional = false)
    @JoinColumn(name = "location_id", nullable = false)
    @JsonManagedReference
    private Location location;

    @OneToMany(mappedBy = "foodProvider", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<OpeningHours> openingHours;

    private String info;

    @Column(name = "additional_info")
    private String additionalInfo;

    @Column(name = "link_to_food_plan")
    private String linkToFoodPlan;

    @Column(name = "link_to_more_information")
    private String linkToMoreInformation;

//    @OneToMany(mappedBy = "foodProvider", cascade = CascadeType.ALL)
//    @JsonManagedReference
//    private Set<Menu> menus;

    @ManyToOne(optional = false)
    @JoinColumn(name = "food_provider_type_id", nullable = false)
    @JsonManagedReference
    private FoodProviderType type;
}