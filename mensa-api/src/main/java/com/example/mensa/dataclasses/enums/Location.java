package com.example.mensa.dataclasses.enums;

import java.nio.charset.StandardCharsets;

public enum Location {
    WUERZBURG("W\u00fcrzburg"),
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
                .replace("\u00e4", "ae")
                .replace("\u00f6", "oe")
                .replace("\u00fc", "ue")
                .replace("\u00df", "ss");
    }
}
