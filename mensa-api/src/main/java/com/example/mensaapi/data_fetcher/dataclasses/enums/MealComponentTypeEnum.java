package com.example.mensaapi.data_fetcher.dataclasses.enums;

public enum MealComponentTypeEnum {
    INGREDIENT("Ingredient"),
    ALLERGEN("Allergen");

    private final String value;

    MealComponentTypeEnum(final String newValue) {
        value = newValue;
    }

    public String getValue() {
        return value;
    }
}
