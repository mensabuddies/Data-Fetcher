package com.example.mensa.dataclasses.enums;

public enum Location {
    WUERZBURG("Würzburg"),
    BAMBERG("Bamberg"),
    SCHWEINFURT("Schweinfurt"),
    ASCHAFFENBURG("Aschaffenburg"),

    INVALID("Invalid");

    private final String value;

    Location(final String newValue) {
        value = newValue;
    }

    public String getValue() {
        return value;
    }

    public String getValueFormatted() {
        return value
                .toLowerCase()
                .replace("ä", "ae")
                .replace("ö", "oe")
                .replace("ü", "ue")
                .replace("ß", "ss");
    }
}
