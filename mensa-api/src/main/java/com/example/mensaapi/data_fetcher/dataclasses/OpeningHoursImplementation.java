package com.example.mensaapi.data_fetcher.dataclasses;
import com.example.mensaapi.data_fetcher.dataclasses.interfaces.OpeningHours;

import java.time.DayOfWeek;

public class OpeningHoursImplementation implements OpeningHours {
    private DayOfWeek weekday;
    private boolean open;
    private String openingAt;
    private String closingAt;

    private String getAMealTill;

    public OpeningHoursImplementation(DayOfWeek weekday, boolean open, String openingAt, String closingAt, String getAMealTill) {
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
