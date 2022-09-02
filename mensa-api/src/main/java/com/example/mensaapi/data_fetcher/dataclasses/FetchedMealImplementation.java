package com.example.mensaapi.data_fetcher.dataclasses;

import com.example.mensaapi.data_fetcher.dataclasses.interfaces.FetchedMeal;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Objects;

public class FetchedMealImplementation implements FetchedMeal {
    private final String name;
    private final int price;

    private final String ingreadientsRaw;

    public FetchedMealImplementation(String name, int price, String ingreadientsRaw) {
        this.name = name;
        this.price = price;
        this.ingreadientsRaw = ingreadientsRaw;
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
    public String getIngreadientsRaw() {
        return ingreadientsRaw;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price);
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof FetchedMealImplementation) &&
                (name.equals(((FetchedMealImplementation) obj).name)) &&
                (price == ((FetchedMealImplementation) obj).price);
    }

    @Override
    public String toString() {
        DecimalFormat d = new DecimalFormat("0.00", new DecimalFormatSymbols(Locale.GERMANY));
        return name + " (" + d.format(price / 100.0) + "\u20ac)" + "(" + ingreadientsRaw + ")";
    }
}