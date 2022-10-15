package com.example.mensa.retrieval.interfaces;

import com.example.mensa.retrieval.FoodProviderParser;
import com.example.mensa.retrieval.MealParser;
import com.example.mensa.retrieval.DayParser;
import com.example.mensa.retrieval.MoreInfoParser;
import org.jsoup.nodes.Element;

import java.util.Optional;

public interface Parser<T> {
    Optional<T> parse(Element fetched);

    static MealParser createMealParser() {
        return new MealParser();
    }

    static DayParser createDayParser() {
        return new DayParser(createMealParser());
    }

    static FoodProviderParser createFoodProviderParser() {
        return new FoodProviderParser();
    }

    static MoreInfoParser createMoreInfoParser() {
        return new MoreInfoParser();
    }
}
