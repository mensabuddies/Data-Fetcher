package com.example.mensaapi.database.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    public String getName() {
        return name;
    }
}