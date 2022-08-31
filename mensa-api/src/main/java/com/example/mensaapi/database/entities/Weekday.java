package com.example.mensaapi.database.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "weekdays")
@NoArgsConstructor
@Getter
@Setter
public class Weekday {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "weekday_id")
    private int id;

    @Column(unique = true)
    private String weekday;

    @ToString.Exclude
    @OneToMany(mappedBy = "weekday", cascade = CascadeType.ALL)
    private Set<OpeningHours> openingHours = new java.util.LinkedHashSet<>();

    public Weekday(String weekday){
        this.weekday = weekday;
    }
}
