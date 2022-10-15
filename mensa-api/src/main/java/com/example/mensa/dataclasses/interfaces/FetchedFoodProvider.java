package com.example.mensa.dataclasses.interfaces;

import com.example.mensa.dataclasses.FetchedCanteen;
import com.example.mensa.dataclasses.FetchedDay;
import com.example.mensa.dataclasses.enums.FetchedFoodProviderType;
import com.example.mensa.dataclasses.FetchedCafeteria;
import com.example.mensa.dataclasses.enums.Location;

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

    String getDescription();

    String getAddress();

    Optional<FetchedDay> getMenuOfDay(LocalDate date);

    FetchedFoodProviderType getType();

    void setDescription(String description);

    void setAddress(String address);

    void setLocation(Location location);

    static FetchedFoodProvider createCanteen(
            String name,
            Location location,
            String info,
            List<FetchedOpeningHours> fetchedOpeningHours,
            String additional,
            String linkToFoodPlan,
            String linkToMoreInformation,
            List<FetchedDay> menusPerFetchedDay,
            String description,
            String address
    ) {
        return new FetchedCanteen(
                name,
                location,
                info,
                fetchedOpeningHours,
                additional,
                linkToMoreInformation,
                linkToFoodPlan,
                menusPerFetchedDay,
                description,
                address
        );
    }

    static FetchedFoodProvider createCafeteria(
            String name,
            Location location,
            String info,
            String linkToMoreInformation,
            List<FetchedOpeningHours> fetchedOpeningHours,
            String additional,
            String description,
            String address
    ) {
        return new FetchedCafeteria(
                name,
                location,
                info,
                linkToMoreInformation,
                fetchedOpeningHours,
                additional,
                description,
                address
        );
    }
}
