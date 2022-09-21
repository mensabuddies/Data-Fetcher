package com.example.mensaapi;

import com.example.mensaapi.data_fetcher.dataclasses.FetchedData;
import com.example.mensaapi.data_fetcher.dataclasses.FetchedDay;
import com.example.mensaapi.data_fetcher.dataclasses.enums.FetchedFoodProviderType;
import com.example.mensaapi.data_fetcher.dataclasses.interfaces.FetchedFoodProvider;
import com.example.mensaapi.data_fetcher.dataclasses.interfaces.FetchedMeal;
import com.example.mensaapi.data_fetcher.dataclasses.interfaces.FetchedOpeningHours;
import com.example.mensaapi.data_fetcher.retrieval.DataFetcher;
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
import java.util.Optional;

@SpringBootApplication
public class MensaApiApplication {

    @Autowired
    WeekdayRepository weekdayRepository;
    @Autowired
    LocationRepository locationRepository;
    @Autowired
    FoodProviderRepository foodProviderRepository;
    @Autowired
    MealRepository mealRepository;
    @Autowired
    MenuRepository menuRepository;

    @Autowired
    OpeningHoursRepository openingHoursRepository;

    @Autowired
    FoodProviderTypeRepository foodProviderTypeRepository;

    public static void main(String[] args) {
        SpringApplication.run(MensaApiApplication.class, args);
    }

    @Bean
    public CommandLineRunner run() {
        return (args -> {
            if (weekdayRepository.findById(1).isEmpty()) {
                Util u = new Util();
                u.insertWeekdays(weekdayRepository);
                u.insertLocations(locationRepository);
                u.insertTypes(foodProviderTypeRepository);
            }
            /// For debugging
            //saveLatestData();
        });
    }


    private void saveLatestData() {
        storeStudentenwerkDataInDatabase();
    }

    @Scheduled(cron = "0 0 0 * * *") // Täglich um 0 Uhr
    //@Scheduled(fixedDelay = 10000)
    private void storeStudentenwerkDataInDatabase() {
        Optional<FetchedData> fetchedData = new DataFetcher().fetchCurrentData();

        fetchedData.ifPresent(data -> {
            for (FetchedFoodProvider fetchedFoodProvider : data.getFetchedFoodProviders()) {
                // First, we store the canteen with its details (like opening hours, etc.)
                FoodProvider foodProvider = storeFoodProvider(fetchedFoodProvider);

                if (fetchedFoodProvider.getType() == FetchedFoodProviderType.CANTEEN) {
                    // Then, we store the meals
                    for (FetchedDay mealsForTheDay : fetchedFoodProvider.getMenus()) {
                        for (FetchedMeal fetchedMeal : mealsForTheDay.getMeals()) {
                            Meal meal = storeMeal(fetchedMeal);
                            // Create and save new menu if it does not exist already
                            menuRepository.findMenuByDateAndMealAndFoodProviderId(mealsForTheDay.getDate(), meal, foodProvider.getId()).ifPresentOrElse(
                                    /* if present: */ menu1 -> System.out.println("Duplicate menu found!"),
                                    /* or else: */() -> menuRepository.save(new Menu(foodProvider, meal, mealsForTheDay.getDate()))
                            );
                        }
                    }
                }
            }
        });

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

    private FoodProvider storeFoodProvider(FetchedFoodProvider fetchedFoodProvider) {
        FoodProvider foodProvider = foodProviderRepository.getFoodProviderByName(fetchedFoodProvider.getName()).orElse(new FoodProvider());

        foodProvider.setName(fetchedFoodProvider.getName());
        foodProvider.setLocation(locationRepository.getLocationByName(fetchedFoodProvider.getLocation().getValue()));
        foodProvider.setInfo(fetchedFoodProvider.getTitleInfo());
        foodProvider.setAdditionalInfo(fetchedFoodProvider.getBodyInfo());
        foodProvider.setLinkToFoodPlan(fetchedFoodProvider.getLinkToFoodPlan());
        foodProvider.setType(foodProviderTypeRepository.findByName(fetchedFoodProvider.getType().getValue()));

        List<OpeningHours> openingHours = openingHoursRepository.findByFoodProviderIdEqualsOrderByWeekday(foodProvider.getId()).orElse(new ArrayList<>());

        if (!openingHours.isEmpty()) {
            // Go through all fetchedOpeningHours (foh) and check if saved openingHours (oh) are up-to-date
            for (int i = 0; i < fetchedFoodProvider.getOpeningHours().size(); i++) {
                var foh = fetchedFoodProvider.getOpeningHours().get(i);

                updateOrInsertOpeningHours(
                        foh,
                        openingHours,
                        foodProvider
                );

            }
        } else {
            // If no openingHours were found, just insert them - no updating necessary
            for (FetchedOpeningHours foh : fetchedFoodProvider.getOpeningHours()) {
                openingHours.add(new OpeningHours(
                        foodProvider,
                        dayOfWeekToWeekday(foh.getWeekday()),
                        foh.isOpen(),
                        foh.getOpeningAt(),
                        foh.getClosingAt(),
                        foh.getGetAMealTill()
                ));

            }
        }


        foodProvider.setOpeningHours(openingHours);

        foodProviderRepository.save(foodProvider);

        return foodProvider;
    }


    private void updateOrInsertOpeningHours(
            FetchedOpeningHours foh,
            List<OpeningHours> openingHours,
            FoodProvider foodProvider
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
                        foodProvider,
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