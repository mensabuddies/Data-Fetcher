package com.example.mensaapi.data_fetcher.retrieval.interfaces;

import com.example.mensaapi.data_fetcher.retrieval.CanteenParser;
import com.example.mensaapi.data_fetcher.retrieval.DayParser;
import com.example.mensaapi.data_fetcher.retrieval.MealParser;
import org.jsoup.nodes.Element;

import java.util.Optional;

public interface Parser<T>{
    Optional<T> parse(Element fetched);

    static MealParser createMealParser() {
        return new MealParser();
    }
    static DayParser createDayParser() { return new DayParser(createMealParser()); }

    static CanteenParser createCanteenParser() { return new CanteenParser(); }
}
