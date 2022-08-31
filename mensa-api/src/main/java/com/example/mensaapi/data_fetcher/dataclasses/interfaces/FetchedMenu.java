package com.example.mensaapi.data_fetcher.dataclasses.interfaces;

import com.example.mensaapi.data_fetcher.dataclasses.FetchedDay;

import java.time.LocalDate;
import java.util.Set;

public interface FetchedMenu {
    LocalDate getDate();

    Set<FetchedMeal> getMeals();

    static FetchedMenu createMenu(LocalDate date, Set<FetchedMeal> fetchedMeals) {
        if (date == null || fetchedMeals == null || fetchedMeals.isEmpty())
            throw new IllegalArgumentException("Date and/or Meals cannot be null/empty!");
        for (FetchedMeal m : fetchedMeals) {
            if (m == null) {
                throw new IllegalArgumentException("The set of meals cannot contain 'null' as a meal!");
            }
        }
        return new FetchedDay(date, fetchedMeals);
    }
}
