package com.example.mensa.dataclasses.interfaces;

import com.example.mensa.dataclasses.FetchedOpeningHoursImplementation;

import java.time.DayOfWeek;

public interface FetchedOpeningHours {
    DayOfWeek getWeekday();

    boolean isOpen();

    String getOpeningAt();

    String getClosingAt();

    String getGetAMealTill();

    @Override
    String toString();

    static FetchedOpeningHoursImplementation createOpeningHours(DayOfWeek weekday, boolean open, String openingAt, String closingAt, String getAMealTill) {
        return new FetchedOpeningHoursImplementation(weekday, open, openingAt, closingAt, getAMealTill);
    }
}
