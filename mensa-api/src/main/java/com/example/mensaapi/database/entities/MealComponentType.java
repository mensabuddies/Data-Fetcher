package com.example.mensaapi.database.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "meal_component_types")
@NoArgsConstructor
@Getter
@Setter
public class MealComponentType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meal_component_type_id")
    private Integer id;

    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "type", cascade = CascadeType.ALL)
    private List<MealComponent> mealComponents;

    public MealComponentType(String name) {
        this.name = name;
    }
}
