package com.example.mensaapi.data_fetcher.retrieval;

import com.example.mensaapi.data_fetcher.dataclasses.Day;
import com.example.mensaapi.data_fetcher.dataclasses.interfaces.Meal;
import com.example.mensaapi.data_fetcher.retrieval.interfaces.Parser;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class DayParser implements Parser<Day> {
    private final MealParser mealParser;

    public DayParser(MealParser mealParser) {
        this.mealParser = mealParser;
    }
    /**
     * Expects node of class "day"
     * @param fetched
     * @return
     */
    @Override
    public Optional<Day> parse(Element fetched) {
        String rawDate;
        LocalDate parsedDate = null;
        Elements menus;
        Set<Meal> mealSet = new HashSet<>();

        if (fetched.hasAttr("data-day")) {
            rawDate = fetched.attr("data-day");

            try {
                // TODO: Wrong on new year
                parsedDate = LocalDate.parse(
                        List.of(rawDate.split(" ")).get(1)
                                .concat(
                                        String.valueOf(YearMonth.now().getYear())
                                ),
                        DateTimeFormatter.ofPattern("dd.MM.yyyy")
                );
            } catch (Exception e) {
                throw new IllegalStateException("Error formatting date");
            }

            menus = fetched.getElementsByClass("menu");


            for (Element menu: menus) {

                mealSet.add(mealParser.parse(menu).orElseThrow());
            }
        }

        if (parsedDate == null) {
            throw new IllegalStateException("Parsing failed!");
        }

        return Optional.of(new Day(parsedDate, mealSet));
    }
}
