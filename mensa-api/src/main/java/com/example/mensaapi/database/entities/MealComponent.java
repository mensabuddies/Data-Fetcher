package com.example.mensaapi.database.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "meal_components")
@NoArgsConstructor
@Getter
@Setter
public class MealComponent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String name;

    @ManyToOne(optional = false)
    @JoinColumn(name = "meal_component_type_id", nullable = false)
    @JsonManagedReference
    private MealComponentType type;

    @Column
    @ManyToMany
    @JoinColumn(name = "meal_id")
    //@Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.MERGE, org.hibernate.annotations.CascadeType.PERSIST})
    List<Meal> meals;

    public MealComponent(MealComponentType type, String name) {
        this.type = type;
        this.name = name;
    }
}
