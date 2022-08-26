package com.example.mensaapi.database.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name = "weekdays")
@NoArgsConstructor
public class Weekday {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "weekday_id")
    private int id;

    private String weekday;

    @OneToMany(mappedBy = "weekday", cascade = CascadeType.ALL)
    private Set<OpeningHours> openingHours;
}
