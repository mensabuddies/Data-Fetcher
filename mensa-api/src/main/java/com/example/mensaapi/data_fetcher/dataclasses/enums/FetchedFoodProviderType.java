package com.example.mensaapi.data_fetcher.dataclasses.enums;

public enum FetchedFoodProviderType {
    CANTEEN("Canteen"),
    CAFETERIA("Cafeteria");

    private final String value;

    FetchedFoodProviderType(final String newValue) {
        value = newValue;
    }

    public String getValue() {
        return value;
    }
}
