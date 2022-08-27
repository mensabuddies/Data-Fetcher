package com.example.mensaapi.database.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name = "allergens")
@NoArgsConstructor
public class Allergen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "allergen_id")
    private Integer id;

    @Column(unique = true)
    private String allergen;

    @ManyToMany(mappedBy = "allergens")
    private Set<Meal> meals;
}
