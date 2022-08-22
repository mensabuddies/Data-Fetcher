package dataclasses.interfaces;

import dataclasses.MealImplementation;
import dataclasses.enums.Role;

public interface Meal {
    String getName();

    int getPrice(Role role); // in cents

    static MealImplementation createMeal(String name, int priceStudent, int priceGuest, int priceEmployee) {
        if (name == null || name.equals("")) {
            throw new IllegalArgumentException("Name cannot be empty!");
        }
        if (priceStudent < 0 || priceEmployee < 0 || priceGuest < 0) {
            throw new IllegalArgumentException("Price of Meal (\"" + name + "\") cannot be below 0!");
        }
        return new MealImplementation(name, priceStudent, priceGuest, priceEmployee);
    }
}
