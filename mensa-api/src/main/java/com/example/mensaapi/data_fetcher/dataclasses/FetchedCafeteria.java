package com.example.mensaapi.data_fetcher.dataclasses;

import com.example.mensaapi.data_fetcher.dataclasses.enums.FoodProviderType;
import com.example.mensaapi.data_fetcher.dataclasses.enums.Location;
import com.example.mensaapi.data_fetcher.dataclasses.interfaces.FetchedFoodProvider;
import com.example.mensaapi.data_fetcher.dataclasses.interfaces.FetchedOpeningHours;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class FetchedCafeteria implements FetchedFoodProvider {
    private final String name;
    private final Location location;
    private final String info;
    private final List<FetchedOpeningHours> fetchedOpeningHours;
    private final String additional;

    public FetchedCafeteria(String name, Location location, String info, List<FetchedOpeningHours> fetchedOpeningHours, String additional) {
        this.name = name;
        this.location = location;
        this.info = info;
        this.fetchedOpeningHours = fetchedOpeningHours;
        this.additional = additional;
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public String getTitleInfo() {
        return info;
    }

    @Override
    public List<FetchedOpeningHours> getOpeningHours() {
        return fetchedOpeningHours;
    }

    @Override
    public String getBodyInfo() {
        return additional;
    }

    @Override
    public String getLinkToFoodPlan() {
        return "";
    }

    @Override
    public List<FetchedDay> getMenus() {
        return Collections.emptyList();
    }

    @Override
    public Optional<FetchedDay> getMenuOfDay(LocalDate date) {
        return Optional.empty();
    }

    @Override
    public FoodProviderType getType() {
        return FoodProviderType.CAFETERIA;
    }
}
