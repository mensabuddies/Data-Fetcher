package com.example.mensaapi.data_fetcher.dataclasses.interfaces;

import com.example.mensaapi.data_fetcher.dataclasses.FetchedCanteenImplementation;
import com.example.mensaapi.data_fetcher.dataclasses.FetchedDay;
import com.example.mensaapi.data_fetcher.dataclasses.enums.Location;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface FetchedCanteen {
    String getName();
    Location getLocation();
    String getTitleInfo();
    List<FetchedOpeningHours> getOpeningHours();
    String getBodyInfo();
    String getLinkToFoodPlan();

    List<FetchedDay> getMenus();

    Optional<FetchedDay> getMenuOfDay(LocalDate date);

    static FetchedCanteenImplementation createCanteen(
            String name,
            Location location,
            String info,
            List<FetchedOpeningHours> fetchedOpeningHours,
            String additional,
            String linkToFoodPlan,
            List<FetchedDay> menusPerFetchedDay
    ) {
        return new FetchedCanteenImplementation(
                name,
                location,
                info,
                fetchedOpeningHours,
                additional,
                linkToFoodPlan,
                menusPerFetchedDay
        );
    }
}
