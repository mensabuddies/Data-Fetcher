package com.example.mensaapi.database.entities;

import lombok.*;

import javax.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "opening_hours")
@NoArgsConstructor
@Getter
@Setter
public class OpeningHours {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "opening_hours_id")
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "canteen_id", nullable = false)
    private Canteen canteen;

    @ManyToOne(optional = false)
    @JoinColumn(name = "weekday_id", nullable = false)
    private Weekday weekday;

    @Column(name = "opens_at")
    private LocalTime opensAt;
    @Column(name = "closes_at")
    private LocalTime closesAt;
    @Column(name = "get_food_till")
    private LocalTime getFoodTill;
}
