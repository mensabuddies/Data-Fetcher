package dataclasses;

import dataclasses.enums.Role;
import dataclasses.interfaces.Meal;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;

public class MealImplementation implements Meal {
    private final String name;
    private final int[] prices;

    public MealImplementation(String name, int priceStudent, int priceGuest, int priceEmployee) {
        this.name = name;
        this.prices = new int[3];
        prices[Role.STUDENT.ordinal()] = priceStudent;
        prices[Role.GUEST.ordinal()] = priceGuest;
        prices[Role.EMPLOYEE.ordinal()] = priceEmployee;
    }

    @Override
    public String getName() {
        // TODO: Trim name
        return name;
    }

    @Override
    public int getPrice(Role role) {
        return prices[role.ordinal()];
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, prices);
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof MealImplementation) &&
                (name.equals(((MealImplementation) obj).name)) &&
                (Arrays.equals(prices, ((MealImplementation) obj).prices));
    }

    @Override
    public String toString() {
        DecimalFormat d = new DecimalFormat("0.00", new DecimalFormatSymbols(Locale.GERMANY));
        return name + " (S:" + d.format(prices[Role.STUDENT.ordinal()] / 100.0) + "\u20ac, B:"
                + d.format(prices[Role.EMPLOYEE.ordinal()] / 100.0) + "\u20ac, G:"
                + d.format(prices[Role.GUEST.ordinal()] / 100.0) + "\u20ac)";
    }
}
