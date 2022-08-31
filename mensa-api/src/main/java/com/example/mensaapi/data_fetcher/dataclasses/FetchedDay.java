package com.example.mensaapi.data_fetcher.dataclasses;

import com.example.mensaapi.data_fetcher.dataclasses.interfaces.FetchedMeal;
import com.example.mensaapi.data_fetcher.dataclasses.interfaces.FetchedMenu;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

public class FetchedDay implements FetchedMenu {
    private final LocalDate date;
    private final Set<FetchedMeal> fetchedMeals;

    public FetchedDay(LocalDate date, Set<FetchedMeal> fetchedMeals) {
        this.date = date;
        this.fetchedMeals = fetchedMeals;
    }


    @Override
    public LocalDate getDate() {
        return date;
    }

    @Override
    public Set<FetchedMeal> getMeals() {
        return Collections.unmodifiableSet(fetchedMeals);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.date.toString());
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof FetchedDay) &&
                date.equals(((FetchedDay) obj).date) &&
                fetchedMeals.equals(((FetchedDay) obj).fetchedMeals);
    }

    @Override
    public String toString() {
        return "Menu of " +
                date.toString() +
                ": " +
                fetchedMeals.toString();
    }
}
