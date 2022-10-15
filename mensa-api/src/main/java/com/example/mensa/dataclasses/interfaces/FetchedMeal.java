package com.example.mensa.dataclasses.interfaces;

import com.example.mensa.dataclasses.FetchedMealImplementation;

public interface FetchedMeal {
    String getName();

    int getPriceStudent(); // in cents

    int getPriceEmployee();

    int getPriceGuest();

    String getAllergensRaw();

    String getIngredientsRaw();

    static FetchedMealImplementation createMeal(String name, int priceStudent, int priceEmployee, int priceGuest, String allergensRaw, String ingredientsRaw) {
        if (name == null || name.equals("")) {
            throw new IllegalArgumentException("Name cannot be empty!");
        }
        //if (priceStudent < 0 || priceEmployee < 0 || priceGuest < 0) {
        //    throw new IllegalArgumentException("Price of Meal (\"" + name + "\") cannot be below 0!");
        // }
        return new FetchedMealImplementation(name, priceStudent, priceEmployee, priceGuest, allergensRaw, ingredientsRaw);
    }
}
