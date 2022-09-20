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

    //@Scheduled(cron = "0 0 0 * * *") // Täglich um 0 Uhr
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
                    menuRepository.save(new Menu(canteen, meal, mealsForTheDay.getDate()));
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
        //canteenRepository.save(canteen);

        //openingHoursRepository.deleteByCanteenId(canteen.getId());
        List<OpeningHours> openingHours = openingHoursRepository.findByCanteenIdEqualsOrderByWeekday(canteen.getId()).orElse(new ArrayList<>());

        if (!openingHours.isEmpty()) {
            for (int i = 0; i < openingHours.size(); i++) {
                var oh = openingHours.get(i);

                updateOrInsertOpeningHours(
                        oh,
                        openingHours,
                        fetchedCanteen,
                        canteen
                );

            }
        } else {
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

    private void updateOrInsertOpeningHours(OpeningHours oh, List<OpeningHours> openingHours, FetchedCanteen fetchedCanteen, Canteen canteen) {
        for (int j = 0; j < fetchedCanteen.getOpeningHours().size(); j++) {
            var foh = fetchedCanteen.getOpeningHours().get(j);

            if (oh.getWeekday().getName().equalsIgnoreCase(foh.getWeekday().getDisplayName(TextStyle.FULL, Locale.GERMAN))) {
                if (oh.isOpened() != foh.isOpen() ||
                        !oh.getOpensAt().equals(foh.getOpeningAt()) ||
                        !oh.getClosesAt().equals(foh.getClosingAt()) ||
                        !oh.getGetFoodTill().equals(foh.getGetAMealTill())) {

                    //iterator.remove();
                    //openingHours.remove(oh);
                    oh.setOpened(foh.isOpen());
                    oh.setWeekday(dayOfWeekToWeekday(foh.getWeekday()));
                    oh.setOpensAt(foh.getOpeningAt());
                    oh.setClosesAt(foh.getClosingAt());
                    oh.setGetFoodTill(foh.getGetAMealTill());

                }
                return; // Break loop
            }
            if (j == fetchedCanteen.getOpeningHours().size() - 1) {
                // If last element, then insert data
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