package com.example.mensaapi.data_fetcher.dataclasses;

import com.example.mensaapi.data_fetcher.dataclasses.enums.FetchedFoodProviderType;
import com.example.mensaapi.data_fetcher.dataclasses.enums.Location;
import com.example.mensaapi.data_fetcher.dataclasses.interfaces.FetchedFoodProvider;
import com.example.mensaapi.data_fetcher.dataclasses.interfaces.FetchedOpeningHours;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class FetchedCafeteria implements FetchedFoodProvider {
    private final String name;
    private Location location;
    private final String info;
    private final List<FetchedOpeningHours> fetchedOpeningHours;

    private final String linkToMoreInformation;

    private final String additional;

    private String description;

    private String address;

    public FetchedCafeteria(
            String name,
            Location location,
            String info,
            String linkToMoreInformation,
            List<FetchedOpeningHours> fetchedOpeningHours,
            String additional,
            String description,
            String address
    ) {
        this.name = name;
        this.location = location;
        this.info = info;
        this.fetchedOpeningHours = fetchedOpeningHours;
        this.linkToMoreInformation = linkToMoreInformation;
        this.additional = additional;
        this.description = description;
        this.address = address;
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
    public String getLinkToMoreInformation() {
        return linkToMoreInformation;
    }

    @Override
    public List<FetchedDay> getMenus() {
        return Collections.emptyList();
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getAddress() {
        return address;
    }

    @Override
    public Optional<FetchedDay> getMenuOfDay(LocalDate date) {
        return Optional.empty();
    }

    @Override
    public FetchedFoodProviderType getType() {
        return FetchedFoodProviderType.CAFETERIA;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public void setLocation(Location location) {
        this.location = location;
    }
}
