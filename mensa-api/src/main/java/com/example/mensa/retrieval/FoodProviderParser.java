package com.example.mensa.retrieval;

import com.example.mensa.dataclasses.FetchedDay;
import com.example.mensa.dataclasses.enums.Location;
import com.example.mensa.dataclasses.interfaces.FetchedFoodProvider;
import com.example.mensa.dataclasses.interfaces.FetchedOpeningHours;
import com.example.mensa.retrieval.interfaces.Fetcher;
import com.example.mensa.retrieval.interfaces.Parser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FoodProviderParser implements Parser<FetchedFoodProvider> {
    // This is needed because not all cafeterias have the location name in them
    public Optional<FetchedFoodProvider> parseWithLocation(Element fetched, Location location) {
        Optional<FetchedFoodProvider> offp = parse(fetched);
        return offp.map(ffp -> {
            ffp.setLocation(location);
            return ffp;
        });
    }

    /**
     * Expects an Element of class "mensa"
     *
     * @param fetched
     * @return
     */
    @Override
    public Optional<FetchedFoodProvider> parse(Element fetched) {
        Elements details = fetched.getElementsByClass("right");
        String name = "";
        String headerInfo = "";
        Location location = Location.WUERZBURG;
        String linkToFood = "";
        String linkToMoreInformation = "";
        String additional = "";
        List<FetchedOpeningHours> op = new ArrayList<>();
        boolean isCafeteria = false;

        for (Element detail : details) {
            // Get Name
            String nameElement = detail.getElementsByTag("h4").text();
            isCafeteria = nameElement.contains("Cafeteria");
            // Info text (e.g. mensa is closed) is directly after the mensa-name in a h4-Tag, seperated by hyphen or pipe
            if (nameElement.contains(" - ") || nameElement.contains(" – ")) {
                String[] parts = nameElement.split(" - | – ");
                name = parts[0];
                headerInfo = parts[1];
            } else if (nameElement.contains(" | ")) {
                String[] parts = nameElement.split(" \\| ");
                name = parts[0];
                headerInfo = parts[1];
            } else {
                // If none of those delimiters can be found, we assume there is no info text.
                name = nameElement;
            }

            // Get the city the mensa is located in
            location = getLocation(nameElement);

            // Get opening hours
            Elements opening = detail.getElementsByClass("opening");
            op = constructOpeningHours(opening.get(0).getElementsByTag("tr"), isCafeteria);

            // Get additional information (e.g. evening mensa)
            additional = (opening.get(0).getElementsByTag("p").text());


            var raw = ("https://www.studentenwerk-wuerzburg.de" +
                    detail.getElementsByClass("fi").attr("href"));

            if (!isCafeteria) {
                linkToFood = raw;
                var loc = raw.substring(raw.lastIndexOf("-")+1, raw.lastIndexOf("."));
                linkToMoreInformation = linkToFood.replace(
                        "essen-trinken/speiseplaene",
                        loc + "/essen-trinken/mensen"
                );
            }
            else{
                linkToMoreInformation = raw;
            }


        }

        List<FetchedDay> menus = new ArrayList<>();

        if (!isCafeteria) {
            Optional<Document> menuOfCanteen = Fetcher.createJsoupFetcher(linkToFood).fetchCurrentData();


            Parser<FetchedDay> dayParser = Parser.createDayParser();

            menuOfCanteen.ifPresent(document -> {
                Elements days = document.getElementsByClass("day").stream().filter(dayElement -> dayElement.tagName().equals("div")).collect(Collectors.toCollection(Elements::new));
                for (Element day : days) {
                    menus.add(dayParser.parse(day).orElseThrow());
                }
            });
        }
        if (isCafeteria) {
            return Optional.of(FetchedFoodProvider.createCafeteria(
                    name,
                    location,
                    headerInfo,
                    linkToMoreInformation,
                    op,
                    additional,
                    "", // Will be parsed later
                    ""
            ));
        } else {
            return Optional.of(FetchedFoodProvider.createCanteen(
                    name,
                    location,
                    headerInfo,
                    op,
                    additional,
                    linkToFood,
                    linkToMoreInformation,
                    menus,
                    "", // Will be parsed later
                    ""
            ));
        }
    }

    private Location getLocation(String nameElement) {
        if (nameElement.contains(Location.WUERZBURG.getValue()))
            return Location.WUERZBURG;
        if (nameElement.contains(Location.BAMBERG.getValue()))
            return Location.BAMBERG;
        if (nameElement.contains(Location.ASCHAFFENBURG.getValue()))
            return Location.ASCHAFFENBURG;
        if (nameElement.contains(Location.SCHWEINFURT.getValue()))
            return Location.SCHWEINFURT;

        return Location.INVALID;
    }

    private int weekdayNameToInt(String shortName) {
        return switch (shortName) {
            case "Mo", "Montag" -> 1;
            case "Di", "Dienstag" -> 2;
            case "Mi", "Mittwoch" -> 3;
            case "Do", "Donnerstag" -> 4;
            case "Fr", "Freitag" -> 5;
            case "Sa", "Samstag" -> 6;
            case "So", "Sonntag" -> 7;
            default -> 0;
        };
    }

    private int weekdaysToIteratorNumber(String weekdays) {
        int day1, day2;
        if (weekdays.contains("-")) {
            String[] split = weekdays.split(" - ");
            day1 = weekdayNameToInt(split[0]);
            day2 = weekdayNameToInt(split[1]);
            return (day2 - day1) + 1;
        }
        if (weekdays.length() == 2)
            return 1;

        return 0;
    }

    private List<FetchedOpeningHours> constructOpeningHours(Elements contentTableRows, boolean isCafeteria) {
        List<FetchedOpeningHours> fetchedOpeningHoursList = new ArrayList<>();
        int weekdayCounter = 0;
        for (Element tableRow : contentTableRows) {
            Elements tableRowItems = tableRow.children();

            // Do we have opening hours?
            if (tableRowItems.size() > 2) {
                String[] hours = tableRowItems.get(1).text().split(" - | ");
                String mealOutTill = "";
                if (!tableRowItems.get(2).text().isEmpty()) {
                    mealOutTill = tableRowItems.get(2).text().split(" ")[2];
                }
                int numberOfLoopExecutions = weekdaysToIteratorNumber(tableRowItems.get(0).text());

                for (int i = 0; i < numberOfLoopExecutions; i++) {


                    DayOfWeek day = DayOfWeek.values()[weekdayCounter];
                    boolean open = true;
                    String openingAt = "";
                    String closingAt = "";
                    String getAMealTill = "";

                    if (hours.length > 0 && !hours[0].equals("geschlossen")) {
                        openingAt = (hours[0]);
                        closingAt = (hours[1]);
                    } else {
                        open = (false);
                    }
                    if (!isCafeteria)
                        getAMealTill = mealOutTill;

                    FetchedOpeningHours h = FetchedOpeningHours.createOpeningHours(
                            day,
                            open,
                            openingAt,
                            closingAt,
                            getAMealTill
                    );

                    weekdayCounter++;
                    fetchedOpeningHoursList.add(h);
                }
            }
        }
        return fetchedOpeningHoursList;
    }
}
