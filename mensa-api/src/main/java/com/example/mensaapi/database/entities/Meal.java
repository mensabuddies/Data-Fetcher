package com.example.mensaapi.database.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "meals")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Meal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meal_id")
    private Integer id;

    @Column(nullable = false)
    private String name;

    // Prices in cent
    @Column(name = "price_student")
    private int priceStudent;

    @Column(name = "price_employee")
    private int priceEmployee;

    @Column(name = "price_guest")
    private int priceGuest;

    @Column
    @ManyToMany
    @JoinColumn(name = "meal_component_id")
   // @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.MERGE, org.hibernate.annotations.CascadeType.PERSIST})
    private List<MealComponent> allergens;

    @Column
    @ManyToMany
    @JoinColumn(name = "meal_component_id")
   // @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.MERGE, org.hibernate.annotations.CascadeType.PERSIST})
    private List<MealComponent> ingredients;

    @OneToMany(mappedBy = "meal", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonBackReference
    private Set<Menu> menus;

    public String getIngredientsAsString() {
        return ingredients.stream().map(MealComponent::getName).collect(Collectors.joining(","));
    }

    public String getAllergensAsString() {
        return allergens.stream().map(MealComponent::getName).collect(Collectors.joining(","));
    }

    public Meal(
            String name,
            int priceStudent,
            int priceEmployee,
            int priceGuest,
            List<MealComponent> allergens,
            List<MealComponent> ingredients
    ) {
        this.name = name;
        this.priceStudent = priceStudent;
        this.priceEmployee = priceEmployee;
        this.priceGuest = priceGuest;
        this.allergens = allergens;
        this.ingredients = ingredients;
        this.menus = new HashSet<>();
    }
}