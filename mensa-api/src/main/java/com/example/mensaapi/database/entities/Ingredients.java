package com.example.mensaapi.database.entities;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "ingredients")
@NoArgsConstructor
@Getter
@Setter
public class Ingredients {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ingredient_id")
    private Integer id;

    @Column(unique = true)
    private String ingredient;

    @ManyToMany(mappedBy = "ingredients")
    private Set<Meal> meals;
}
