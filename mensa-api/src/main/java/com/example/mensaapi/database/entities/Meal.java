package com.example.mensaapi.database.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

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
    private String allergens;

    @Column
    private String ingredients;

    @OneToMany(mappedBy = "meal", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<Menu> menus;

    public Meal(String name, int priceStudent, int priceEmployee, int priceGuest, String allergens, String ingredients) {
        this.name = name;
        this.priceStudent = priceStudent;
        this.priceEmployee = priceEmployee;
        this.priceGuest = priceGuest;
        this.allergens = allergens;
        this.ingredients = ingredients;
        this.menus = new HashSet<>();
    }
}