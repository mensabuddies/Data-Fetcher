package com.example.mensaapi.data_fetcher.dataclasses;

import com.example.mensaapi.data_fetcher.dataclasses.enums.Location;
import com.example.mensaapi.data_fetcher.dataclasses.interfaces.Canteen;
import com.example.mensaapi.data_fetcher.dataclasses.interfaces.OpeningHours;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class CanteenImplementation implements Canteen {
    private final String name;
    private final Location location;
    private final String info;
    private final List<OpeningHours> openingHours;
    private final String additional;

    private final String linkToFoodPlan;

    private final List<Day> menusPerDay;

    public CanteenImplementation(
            String name,
            Location location,
            String info,
            List<OpeningHours> openingHours,
            String additional,
            String linkToFoodPlan,
            List<Day> menusPerDay
    ) {
        this.name = name;
        this.location = location;
        this.info = info;
        this.openingHours = openingHours;
        this.additional = additional;
        this.linkToFoodPlan = linkToFoodPlan;
        this.menusPerDay = menusPerDay;
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
    public List<OpeningHours> getOpeningHours() {
        return openingHours;
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
    public List<Day> getMenus() {
        return menusPerDay;
    }

    @Override
    public Optional<Day> getMenuOfDay(LocalDate date) {
        return menusPerDay.stream().filter(menu -> menu.getDate().equals(date)).findFirst();
    }

    @Override
    public String toString() {
        return "Canteen{" +
                "name='" + name + '\'' +
                ", location=" + location +
                ", info='" + info + '\'' +
                ", openingHours=" + openingHours +
                ", additional='" + additional + '\'' +
                ", linkToFoodPlan=" + linkToFoodPlan +
                ", meals=" + menusPerDay +
                "}\n";
    }
}
