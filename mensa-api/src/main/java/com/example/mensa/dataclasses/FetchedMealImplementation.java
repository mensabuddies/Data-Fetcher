package com.example.mensa.dataclasses;

import com.example.mensa.dataclasses.interfaces.FetchedMeal;

import java.util.Objects;

public class FetchedMealImplementation implements FetchedMeal {
    private final String name;
    private final int priceStudent;
    private final int priceEmployee;
    private final int priceGuest;
    private final String allergensRaw;
    private final String ingredientsRaw;

    public FetchedMealImplementation(String name, int priceStudent, int priceEmployee, int priceGuest, String allergensRaw, String ingredientsRaw) {
        this.name = name;
        this.priceStudent = priceStudent;
        this.priceEmployee = priceEmployee;
        this.priceGuest = priceGuest;
        this.allergensRaw = allergensRaw;
        this.ingredientsRaw = ingredientsRaw;
    }

    @Override
    public String getName() {
        // TODO: Trim name
        return name;
    }

    @Override
    public int getPriceStudent() {
        return priceStudent;
    }

    @Override
    public int getPriceEmployee() {
        return priceEmployee;
    }

    @Override
    public int getPriceGuest() {
        return priceGuest;
    }

    @Override
    public String getAllergensRaw() {
        return allergensRaw;
    }

    @Override
    public String getIngredientsRaw() {
        return ingredientsRaw;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FetchedMealImplementation that = (FetchedMealImplementation) o;
        return priceStudent == that.priceStudent && priceEmployee == that.priceEmployee && priceGuest == that.priceGuest && name.equals(that.name) && Objects.equals(allergensRaw, that.allergensRaw) && Objects.equals(ingredientsRaw, that.ingredientsRaw);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, priceStudent, priceEmployee, priceGuest, allergensRaw, ingredientsRaw);
    }

    @Override
    public String toString() {
        return "FetchedMealImplementation{" +
                "name='" + name + '\'' +
                ", priceStudent=" + priceStudent +
                ", priceEmployee=" + priceEmployee +
                ", priceGuest=" + priceGuest +
                ", allergensRaw='" + allergensRaw + '\'' +
                ", ingredientsRaw='" + ingredientsRaw + '\'' +
                '}';
    }
}