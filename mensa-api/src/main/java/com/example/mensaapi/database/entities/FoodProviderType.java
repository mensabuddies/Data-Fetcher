package com.example.mensaapi.database.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "food_provider_types")
@NoArgsConstructor
@Getter
@Setter
public class FoodProviderType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "food_provider_type_id")
    private Integer id;

    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "type", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<FoodProvider> foodProviders;

    public FoodProviderType(String name) {
        this.name = name;
    }
}
