package com.example.mensa.dataclasses;

import com.example.mensa.dataclasses.interfaces.FetchedOpeningHours;

import java.time.DayOfWeek;

public class FetchedOpeningHoursImplementation implements FetchedOpeningHours {
    private final DayOfWeek weekday;
    private final boolean open;
    private final String openingAt;
    private final String closingAt;

    private final String getAMealTill;

    public FetchedOpeningHoursImplementation(DayOfWeek weekday, boolean open, String openingAt, String closingAt, String getAMealTill) {
        this.weekday = weekday;
        this.open = open;
        this.openingAt = openingAt;
        this.closingAt = closingAt;
        this.getAMealTill = getAMealTill;
    }

    @Override
    public DayOfWeek getWeekday() {
        return weekday;
    }

    @Override
    public boolean isOpen() {
        return open;
    }

    @Override
    public String getOpeningAt() {
        return openingAt;
    }

    @Override
    public String getClosingAt() {
        return closingAt;
    }

    @Override
    public String getGetAMealTill() {
        return getAMealTill;
    }

    @Override
    public String toString() {
        return "OpeningHours{" +
                "weekday=" + weekday +
                ", open=" + open +
                ", openingAt='" + openingAt + '\'' +
                ", closingAt='" + closingAt + '\'' +
                ", getAMealTill='" + getAMealTill + '\'' +
                '}';
    }
}
