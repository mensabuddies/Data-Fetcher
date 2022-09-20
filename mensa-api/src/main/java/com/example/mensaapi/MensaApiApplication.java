package com.example.mensaapi;

import com.example.mensaapi.data_fetcher.DataFetcher;
import com.example.mensaapi.data_fetcher.dataclasses.FetchedDay;
import com.example.mensaapi.data_fetcher.dataclasses.interfaces.FetchedCanteen;
import com.example.mensaapi.data_fetcher.dataclasses.interfaces.FetchedMeal;
import com.example.mensaapi.data_fetcher.dataclasses.interfaces.FetchedOpeningHours;
import com.example.mensaapi.database.Util;
import com.example.mensaapi.database.entities.*;
import com.example.mensaapi.database.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@SpringBootApplication
public class MensaApiApplication {

    @Autowired
    WeekdayRepository weekdayRepository;
    @Autowired
    LocationRepository locationRepository;
    @Autowired
    CanteenRepository canteenRepository;
    @Autowired
    MealRepository mealRepository;
    @Autowired
    MenuRepository menuRepository;

    @Autowired
    OpeningHoursRepository openingHoursRepository;

    public static void main(String[] args) {
        SpringApplication.run(MensaApiApplication.class, args);
    }

    @Bean
    public CommandLineRunner run() {
        return (args -> {
            if (!weekdayRepository.findById(1).isPresent()) {
                Util u = new Util();
                u.insertWeekdays(weekdayRepository);
                u.insertLocations(locationRepository);
            }
            /// For debugging
            //saveLatestData();
        });
    }


    private void saveLatestData() {
        storeStudentenwerkDataInDatabase();
    }

    @Scheduled(cron = "0 0 0 * * *") // Täglich um 0 Uhr
    @Scheduled(fixedDelay = 10000)
    private void storeStudentenwerkDataInDatabase() {
        List<FetchedCanteen> fetchedCanteens = new DataFetcher().get();

        for (FetchedCanteen fetchedCanteen : fetchedCanteens) {
            // First, we store the canteen with its details (like opening hours, etc.)
            Canteen canteen = storeCanteen(fetchedCanteen);

            // Then, we store the meals
            for (FetchedDay mealsForTheDay : fetchedCanteen.getMenus()) {
                for (FetchedMeal fetchedMeal : mealsForTheDay.getMeals()) {
                    Meal meal = storeMeal(fetchedMeal);
                    // Create and save new menu if it does not exist already
                    menuRepository.findMenuByDateAndMeal(mealsForTheDay.getDate(), meal).ifPresentOrElse(
                            /* if present: */ menu1 -> System.out.println("Duplicate menu found!"),
                            /* or else: */() -> menuRepository.save(new Menu(canteen, meal, mealsForTheDay.getDate()))
                    );
                }
            }
        }
    }

    private Meal storeMeal(FetchedMeal fetchedMeal) {
        Meal meal = new Meal(
                fetchedMeal.getName(),
                fetchedMeal.getPriceStudent(),
                fetchedMeal.getPriceEmployee(),
                fetchedMeal.getPriceGuest(),
                fetchedMeal.getAllergensRaw(),
                fetchedMeal.getIngredientsRaw()
        );

        // Check if this meal has ever been served in the exact same configuration
        List<Meal> meals = mealRepository.getMealByName(fetchedMeal.getName());
        // Is there even a meal with this name?
        if (meals != null && !meals.isEmpty()) {
            // Are all details the same?
            for (Meal m :
                    meals) {
                if (m.getPriceStudent() == fetchedMeal.getPriceStudent() &&
                        m.getPriceEmployee() == fetchedMeal.getPriceEmployee() &&
                        m.getPriceGuest() == fetchedMeal.getPriceGuest() &&
                        m.getAllergens().equals(fetchedMeal.getAllergensRaw()) &&
                        m.getIngredients().equals(fetchedMeal.getIngredientsRaw())
                ) {
                    // if yes, override the previously created object with the data from the database
                    System.out.println("Duplicate found!");
                    return m;
                }
            }
        }

        return mealRepository.save(meal);
    }

    private Canteen storeCanteen(FetchedCanteen fetchedCanteen) {
        Canteen canteen = canteenRepository.getCanteenByName(fetchedCanteen.getName()).orElse(new Canteen());

        canteen.setName(fetchedCanteen.getName());
        canteen.setLocation(locationRepository.getLocationByName(fetchedCanteen.getLocation().getValue()));
        canteen.setInfo(fetchedCanteen.getTitleInfo());
        canteen.setAdditionalInfo(fetchedCanteen.getBodyInfo());
        canteen.setLinkToFoodPlan(fetchedCanteen.getLinkToFoodPlan());

        List<OpeningHours> openingHours = openingHoursRepository.findByCanteenIdEqualsOrderByWeekday(canteen.getId()).orElse(new ArrayList<>());

        if (!openingHours.isEmpty()) {
            // Go through all fetchedOpeningHours (foh) and check if saved openingHours (oh) are up-to-date
            for (int i = 0; i < fetchedCanteen.getOpeningHours().size(); i++) {
                var foh = fetchedCanteen.getOpeningHours().get(i);

                updateOrInsertOpeningHours(
                        foh,
                        openingHours,
                        fetchedCanteen,
                        canteen
                );

            }
        } else {
            // If no openingHours were found, just insert them - no updating necessary
            for (FetchedOpeningHours foh : fetchedCanteen.getOpeningHours()) {
                openingHours.add(new OpeningHours(
                        canteen,
                        dayOfWeekToWeekday(foh.getWeekday()),
                        foh.isOpen(),
                        foh.getOpeningAt(),
                        foh.getClosingAt(),
                        foh.getGetAMealTill()
                ));

            }
        }


        canteen.setOpeningHours(openingHours);

        canteenRepository.save(canteen);

        return canteen;
    }

    private void updateOrInsertOpeningHours(
            FetchedOpeningHours foh,
            List<OpeningHours> openingHours,
            FetchedCanteen fetchedCanteen,
            Canteen canteen
    ) {
        // go through saved openingHours
        for (int j = 0; j < openingHours.size(); j++) {
            var oh = openingHours.get(j);

            // and try to find the corresponding openingHour (oh) to the fetchedOpeningHour (foh)
            if (oh.getWeekday().getName().equalsIgnoreCase(foh.getWeekday()
                    .getDisplayName(TextStyle.FULL, Locale.GERMAN))) {

                // Now check if hours for that day are still up-to-date
                if (oh.isOpened() != foh.isOpen() ||
                        !oh.getOpensAt().equals(foh.getOpeningAt()) ||
                        !oh.getClosesAt().equals(foh.getClosingAt()) ||
                        !oh.getGetFoodTill().equals(foh.getGetAMealTill())) {

                    // If not update entry
                    oh.setOpened(foh.isOpen());
                    oh.setWeekday(dayOfWeekToWeekday(foh.getWeekday()));
                    oh.setOpensAt(foh.getOpeningAt());
                    oh.setClosesAt(foh.getClosingAt());
                    oh.setGetFoodTill(foh.getGetAMealTill());
                }

                // If we found the corresponding foh we can bail out
                return;
            }

            // Check if this is the last iteration
            if (j == openingHours.size() - 1) {
                // We only land here if the corresponding openingHour was not found, so insert it
                openingHours.add(new OpeningHours(
                        canteen,
                        dayOfWeekToWeekday(foh.getWeekday()),
                        foh.isOpen(),
                        foh.getOpeningAt(),
                        foh.getClosingAt(),
                        foh.getGetAMealTill()
                ));

                // and then bail out directly
                return;
            }
        }

    }

    private Weekday dayOfWeekToWeekday(DayOfWeek weekday) {
        String weekdayName = switch (weekday) {
            case MONDAY -> "Montag";
            case TUESDAY -> "Dienstag";
            case WEDNESDAY -> "Mittwoch";
            case THURSDAY -> "Donnerstag";
            case FRIDAY -> "Freitag";
            case SATURDAY -> "Samstag";
            case SUNDAY -> "Sonntag";
        };
        return weekdayRepository.getWeekdayByName(weekdayName);
    }


}