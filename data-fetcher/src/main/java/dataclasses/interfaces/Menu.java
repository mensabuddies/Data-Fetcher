package dataclasses.interfaces;

import dataclasses.Day;

import java.time.LocalDate;
import java.util.Set;

public interface Menu {
    LocalDate getDate();

    Set<Meal> getMeals();

    static Menu createMenu(LocalDate date, Set<Meal> meals) {
        if (date == null || meals == null || meals.isEmpty())
            throw new IllegalArgumentException("Date and/or Meals cannot be null/empty!");
        for (Meal m : meals) {
            if (m == null) {
                throw new IllegalArgumentException("The set of meals cannot contain 'null' as a meal!");
            }
        }
        return new Day(date, meals);
    }
}
