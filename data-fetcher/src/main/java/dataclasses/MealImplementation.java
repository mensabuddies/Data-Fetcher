package dataclasses;

import dataclasses.interfaces.Meal;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Objects;

public class MealImplementation implements Meal {
    private final String name;
    private final int price;

    public MealImplementation(String name, int price) {
        this.name = name;
        this.price = price;
    }

    @Override
    public String getName() {
        // TODO: Trim name
        return name;
    }

    @Override
    public int getPrice() {
        return price;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price);
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof MealImplementation) &&
                (name.equals(((MealImplementation) obj).name)) &&
                (price == ((MealImplementation) obj).price);
    }

    @Override
    public String toString() {
        DecimalFormat d = new DecimalFormat("0.00", new DecimalFormatSymbols(Locale.GERMANY));
        return name + " (" + d.format(price / 100.0) + "\u20ac)";
    }
}
