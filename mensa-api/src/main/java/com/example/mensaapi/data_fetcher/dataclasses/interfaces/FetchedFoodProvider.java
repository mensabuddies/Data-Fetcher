package com.example.mensaapi.data_fetcher.dataclasses.interfaces;

import com.example.mensaapi.data_fetcher.dataclasses.FetchedCafeteria;
import com.example.mensaapi.data_fetcher.dataclasses.FetchedCanteen;
import com.example.mensaapi.data_fetcher.dataclasses.FetchedDay;
import com.example.mensaapi.data_fetcher.dataclasses.enums.FetchedFoodProviderType;
import com.example.mensaapi.data_fetcher.dataclasses.enums.Location;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface FetchedFoodProvider {
    String getName();

    Location getLocation();

    String getTitleInfo();

    List<FetchedOpeningHours> getOpeningHours();

    String getBodyInfo();

    String getLinkToFoodPlan();

    String getLinkToMoreInformation();

    List<FetchedDay> getMenus();

    Optional<FetchedDay> getMenuOfDay(LocalDate date);

    FetchedFoodProviderType getType();

    void setLocation(Location location);

    static FetchedFoodProvider createCanteen(
            String name,
            Location location,
            String info,
            List<FetchedOpeningHours> fetchedOpeningHours,
            String additional,
            String linkToFoodPlan,
            String linkToMoreInformation,
            List<FetchedDay> menusPerFetchedDay
    ) {
        return new FetchedCanteen(
                name,
                location,
                info,
                fetchedOpeningHours,
                additional,
                linkToMoreInformation,
                linkToFoodPlan,
                menusPerFetchedDay
        );
    }

    static FetchedFoodProvider createCafeteria(
            String name,
            Location location,
            String info,
            String linkToMoreInformation,
            List<FetchedOpeningHours> fetchedOpeningHours,
            String additional
    ) {
        return new FetchedCafeteria(
                name,
                location,
                info,
                linkToMoreInformation,
                fetchedOpeningHours,
                additional
        );
    }
}
