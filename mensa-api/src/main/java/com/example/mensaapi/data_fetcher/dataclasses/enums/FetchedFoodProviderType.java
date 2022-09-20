package com.example.mensaapi.data_fetcher.dataclasses.enums;

public enum FetchedFoodProviderType {
    CANTEEN("Cafeteria"),
    CAFETERIA("Canteen");

    private final String value;

    FetchedFoodProviderType(final String newValue) {
        value = newValue;
    }

    public String getValue() {
        return value;
    }
}
