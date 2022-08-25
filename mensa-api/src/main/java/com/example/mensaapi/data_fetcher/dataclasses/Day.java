package com.example.mensaapi.data_fetcher.dataclasses;

import com.example.mensaapi.data_fetcher.dataclasses.interfaces.Meal;
import com.example.mensaapi.data_fetcher.dataclasses.interfaces.Menu;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

public class Day implements Menu {
    private final LocalDate date;
    private final Set<Meal> meals;

    public Day(LocalDate date, Set<Meal> meals) {
        this.date = date;
        this.meals = meals;
    }


    @Override
    public LocalDate getDate() {
        return date;
    }

    @Override
    public Set<Meal> getMeals() {
        return Collections.unmodifiableSet(meals);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.date.toString());
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Day) &&
                date.equals(((Day) obj).date) &&
                meals.equals(((Day) obj).meals);
    }

    @Override
    public String toString() {
        return "Menu of " +
                date.toString() +
                ": " +
                meals.toString();
    }
}
