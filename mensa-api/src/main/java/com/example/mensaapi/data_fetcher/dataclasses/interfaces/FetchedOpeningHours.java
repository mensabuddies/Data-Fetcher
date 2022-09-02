package com.example.mensaapi.data_fetcher.dataclasses.interfaces;

import com.example.mensaapi.data_fetcher.dataclasses.FetchedOpeningHoursImplementation;

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
