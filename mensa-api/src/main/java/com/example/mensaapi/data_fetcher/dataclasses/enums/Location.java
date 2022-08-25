package com.example.mensaapi.data_fetcher.dataclasses.enums;

public enum Location {
    WÜRZBURG("Würzburg"),
    BAMBERG("Bamberg"),
    SCHWEINFURT("Schweinfurt"),
    ASCHAFFENBURG("Aschaffenburg");

    private final String value;

    Location(final String newValue) {
        value = newValue;
    }

    public String getValue() { return value; }
}
