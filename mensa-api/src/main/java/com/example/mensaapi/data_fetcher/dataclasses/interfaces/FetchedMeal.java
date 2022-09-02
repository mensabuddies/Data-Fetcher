package com.example.mensaapi.data_fetcher.dataclasses.interfaces;

import com.example.mensaapi.data_fetcher.dataclasses.FetchedMealImplementation;

public interface FetchedMeal {
    String getName();

    int getPrice(); // in cents

    String getIngreadientsRaw();

    static FetchedMealImplementation createMeal(String name, int price, String ingredientsRaw) {
        if (name == null || name.equals("")) {
            throw new IllegalArgumentException("Name cannot be empty!");
        }
        if (price < 0) {
            throw new IllegalArgumentException("Price of Meal (\"" + name + "\") cannot be below 0!");
        }
        return new FetchedMealImplementation(name, price, ingredientsRaw);
    }
}
