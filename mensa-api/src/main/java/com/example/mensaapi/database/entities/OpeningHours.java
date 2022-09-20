package com.example.mensaapi.database.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

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
    @JoinColumn(name = "food_provider_id", nullable = false)
    @JsonBackReference
    private FoodProvider foodProvider;

    @ManyToOne(optional = false)
    @JoinColumn(name = "weekday_id", nullable = false)
    @JsonManagedReference
    private Weekday weekday;

    @Column(name = "is_opened")
    private boolean isOpened;

    @Column(name = "opens_at")
    private String opensAt;

    @Column(name = "closes_at")
    private String closesAt;

    @Column(name = "get_food_till")
    private String getFoodTill;

    public OpeningHours(FoodProvider foodProvider, Weekday weekday, boolean isOpened, String opensAt, String closesAt, String getFoodTill) {
        this.foodProvider = foodProvider;
        this.isOpened = isOpened;
        this.weekday = weekday;
        this.opensAt = opensAt;
        this.closesAt = closesAt;
        this.getFoodTill = getFoodTill;
    }
}