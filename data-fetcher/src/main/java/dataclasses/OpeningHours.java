package dataclasses;

import dataclasses.enums.Weekday;

public class OpeningHours {
    private Weekday weekday;
    private boolean open;
    private String openingAt;
    private String closingAt;
    private String getAMealTill;

    public OpeningHours(){
        // Empty constructor
    }

    public Weekday getWeekday() {
        return weekday;
    }

    public void setWeekday(Weekday weekday) {
        this.weekday = weekday;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public String getOpeningAt() {
        return openingAt;
    }

    public void setOpeningAt(String openingAt) {
        this.openingAt = openingAt;
    }

    public String getClosingAt() {
        return closingAt;
    }

    public void setClosingAt(String closingAt) {
        this.closingAt = closingAt;
    }

    public String getGetAMealTill() {
        return getAMealTill;
    }

    public void setGetAMealTill(String getAMealTill) {
        this.getAMealTill = getAMealTill;
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
