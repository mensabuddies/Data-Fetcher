package com.example.mensaapi.data_fetcher.dataclasses.interfaces;

import com.example.mensaapi.data_fetcher.dataclasses.CanteenImplementation;
import com.example.mensaapi.data_fetcher.dataclasses.Day;
import com.example.mensaapi.data_fetcher.dataclasses.OpeningHoursImplementation;
import com.example.mensaapi.data_fetcher.dataclasses.enums.Location;

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
