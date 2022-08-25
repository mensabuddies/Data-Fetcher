package com.example.mensaapi.data_fetcher.dataclasses.interfaces;

import com.example.mensaapi.data_fetcher.dataclasses.MealImplementation;

public interface Meal {
    String getName();

    int getPrice(); // in cents

    String getIngreadientsRaw();

    static MealImplementation createMeal(String name, int price, String ingredientsRaw) {
        if (name == null || name.equals("")) {
            throw new IllegalArgumentException("Name cannot be empty!");
        }
        if (price < 0) {
            throw new IllegalArgumentException("Price of Meal (\"" + name + "\") cannot be below 0!");
        }
        return new MealImplementation(name, price, ingredientsRaw);
    }
}
