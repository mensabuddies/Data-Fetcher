package dataclasses.interfaces;

import dataclasses.CanteenImplementation;
import dataclasses.Day;
import dataclasses.OpeningHoursImplementation;
import dataclasses.enums.Location;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface Canteen {
    String getName();
    Location getLocation();
    String getTitleInfo();
    List<OpeningHours> getOpeningHours();
    String getBodyInfo();
    String getLinkToFoodPlan();

    List<Day> getMenus();

    Optional<Day> getMenuOfDay(LocalDate date);

    static CanteenImplementation createCanteen(
            String name,
            Location location,
            String info,
            List<OpeningHours> openingHours,
            String additional,
            String linkToFoodPlan,
            List<Day> menusPerDay
    ) {
        return new CanteenImplementation(
                name,
                location,
                info,
                openingHours,
                additional,
                linkToFoodPlan,
                menusPerDay
        );
    }
}
