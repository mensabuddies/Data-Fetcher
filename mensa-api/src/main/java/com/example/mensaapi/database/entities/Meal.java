package com.example.mensaapi.database.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name = "meals")
@NoArgsConstructor
public class Meal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meal_id")
    private Integer id;

    @Column(unique = true, nullable = false)
    private String name;

    // Prices in cent
    @Column(name = "price_student")
    private int priceStudent;

    @Column(name = "price_employee")
    private int priceEmployee;

    @Column(name = "price_guest")
    private int priceGuest;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "meal_allergens_relationship",
            joinColumns = @JoinColumn(name = "meal_id"),
            inverseJoinColumns = @JoinColumn(name = "allergen_id")
    )
    private Set<Allergen> allergens;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "meal_ingredients_relationship",
            joinColumns = @JoinColumn(name = "meal_id"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_id")
    )
    private Set<Ingredients> ingredients;

    @OneToMany(mappedBy = "meal", cascade = CascadeType.ALL)
    private Set<Menu> menus;
}