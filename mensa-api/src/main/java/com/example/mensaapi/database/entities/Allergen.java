package com.example.mensaapi.database.entities;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "allergens")
@NoArgsConstructor
@Getter
@Setter
public class Allergen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "allergen_id")
    private Integer id;

    @Column(unique = true)
    private String name;

    @ManyToMany(mappedBy = "allergens")
    private Set<Meal> meals;
}
