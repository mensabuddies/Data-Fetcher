package com.example.mensaapi.database.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "menus")
@NoArgsConstructor
@Getter
@Setter
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id")
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "food_provider_id")
    @JsonBackReference
    private FoodProvider foodProvider;

    @ManyToOne()
    @JoinColumn(name = "meal_id")
    @JsonManagedReference
    private Meal meal;

    private LocalDate date;

    public Menu(FoodProvider foodProvider, Meal meal, LocalDate date) {
        this.foodProvider = foodProvider;
        this.meal = meal;
        this.date = date;
    }

    public Menu(FoodProvider foodProvider, LocalDate date) {
        this.foodProvider = foodProvider;
        this.date = date;
    }
}