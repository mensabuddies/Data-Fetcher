package com.example.mensaapi.data_fetcher.dataclasses;

import com.example.mensaapi.data_fetcher.dataclasses.enums.FetchedFoodProviderType;
import com.example.mensaapi.data_fetcher.dataclasses.enums.Location;
import com.example.mensaapi.data_fetcher.dataclasses.interfaces.FetchedFoodProvider;
import com.example.mensaapi.data_fetcher.dataclasses.interfaces.FetchedOpeningHours;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class FetchedCanteen implements FetchedFoodProvider {
    private final String name;
    private Location location;
    private final String info;
    private final List<FetchedOpeningHours> fetchedOpeningHours;
    private final String additional;

    private final String linkToFoodPlan;

    private final String linkToMoreInfo;
    private final List<FetchedDay> menusPerFetchedDay;

    public FetchedCanteen(
            String name,
            Location location,
            String info,
            List<FetchedOpeningHours> fetchedOpeningHours,
            String additional,
            String linkToMoreInfo,
            String linkToFoodPlan,
            List<FetchedDay> menusPerFetchedDay
    ) {
        this.name = name;
        this.location = location;
        this.info = info;
        this.fetchedOpeningHours = fetchedOpeningHours;
        this.additional = additional;
        this.linkToFoodPlan = linkToFoodPlan;
        this.linkToMoreInfo = linkToMoreInfo;
        this.menusPerFetchedDay = menusPerFetchedDay;
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
        return linkToFoodPlan;
    }

    @Override
    public String getLinkToMoreInformation() {
        return linkToMoreInfo;
    }

    @Override
    public List<FetchedDay> getMenus() {
        return menusPerFetchedDay;
    }

    @Override
    public Optional<FetchedDay> getMenuOfDay(LocalDate date) {
        return menusPerFetchedDay.stream().filter(menu -> menu.getDate().equals(date)).findFirst();
    }

    @Override
    public FetchedFoodProviderType getType() {
        return FetchedFoodProviderType.CANTEEN;
    }

    @Override
    public String toString() {
        return "Canteen{" +
                "name='" + name + '\'' +
                ", location=" + location +
                ", info='" + info + '\'' +
                ", openingHours=" + fetchedOpeningHours +
                ", additional='" + additional + '\'' +
                ", linkToFoodPlan=" + linkToFoodPlan +
                ", meals=" + menusPerFetchedDay +
                "}\n";
    }

    @Override
    public void setLocation(Location location) {
        this.location = location;
    }
}
