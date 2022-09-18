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
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;

@SpringBootApplication
public class MensaApiApplication {

	@Autowired WeekdayRepository weekdayRepository;
	@Autowired LocationRepository locationRepository;
	@Autowired CanteenRepository canteenRepository;
	@Autowired MealRepository mealRepository;
	@Autowired MenuRepository menuRepository;

	public static void main(String[] args) {
		SpringApplication.run(MensaApiApplication.class, args);
	}

	@Bean
	public CommandLineRunner run(){
		return (args -> {
			Util u = new Util();
			u.insertWeekdays(weekdayRepository);
			u.insertLocations(locationRepository);

			saveLatestData();
		});
	}


	private void saveLatestData() {
		storeStudentenwerkDataInDatabase();
	}

	@Scheduled(cron = "0 0 0 * * *") // Täglich um 0 Uhr
	private void storeStudentenwerkDataInDatabase(){
		List<FetchedCanteen> fetchedCanteens = new DataFetcher().get();

		for(FetchedCanteen fetchedCanteen : fetchedCanteens){
			// First, we store the canteen with its details (like opening hours, etc.)
			Canteen canteen = storeCanteen(fetchedCanteen);

			// Then, we store the meals
			for(FetchedDay mealsForTheDay : fetchedCanteen.getMenus()){
				for(FetchedMeal fetchedMeal : mealsForTheDay.getMeals()){
					Meal meal = storeMeal(fetchedMeal);
					storeMenu(canteen, meal, mealsForTheDay.getDate());
				}
			}

			// TODO: Link meals to canteens via menus
		}
	}

	private Menu storeMenu(Canteen canteen, Meal meal, LocalDate date){
		// Create a menu with that meal
		Menu menu = menuRepository.save(new Menu(canteen, meal, date));

		return menu;
	}

	private Meal storeMeal(FetchedMeal fetchedMeal){
		Meal meal = new Meal(
				fetchedMeal.getName(),
				fetchedMeal.getPriceStudent(),
				fetchedMeal.getPriceEmployee(),
				fetchedMeal.getPriceGuest(),
				fetchedMeal.getAllergensRaw(),
				fetchedMeal.getIngredientsRaw()
		);

		// Check if this meal has ever been served in the exact same configuration
			Meal m = mealRepository.getMealByName(fetchedMeal.getName());
			// Is there even a meal with this name?
			if(m != null){
				// Are all details the same?
				if(m.getPriceStudent() == fetchedMeal.getPriceStudent() &&
						m.getPriceEmployee() == fetchedMeal.getPriceEmployee() &&
						m.getPriceGuest() == fetchedMeal.getPriceGuest() &&
						m.getAllergens().equals(fetchedMeal.getAllergensRaw()) &&
						m.getIngredients().equals(fetchedMeal.getIngredientsRaw())
				){
					// if yes, override the previously created object with the data from the database
					System.out.println("Duplicate found!");
					meal = m;
				}
			} else {
				meal = mealRepository.save(meal);
			}

		return meal;
	}

	private Canteen storeCanteen(FetchedCanteen fetchedCanteen){

		// TODO: More efficient with findByName, but has to be implemented
		BiFunction<Iterable<Canteen>, String,Integer> getId = (canteens, canteenName) -> {
			for (Canteen c:
					canteens) {
				if (c.getName().equals(canteenName)) return c.getId();
			}
			return -1;
		};

		Integer idInDB = getId.apply(canteenRepository.findAll(), fetchedCanteen.getName());

		Canteen canteen = canteenRepository.findById(idInDB).orElse(new Canteen());

		canteen.setName(fetchedCanteen.getName());
		canteen.setLocation(locationRepository.getLocationByName(fetchedCanteen.getLocation().getValue()));
		canteen.setInfo(fetchedCanteen.getTitleInfo());
		canteen.setAdditionalInfo(fetchedCanteen.getBodyInfo());
		canteen.setLinkToFoodPlan(fetchedCanteen.getLinkToFoodPlan());
		//canteenRepository.save(canteen);


		Set<OpeningHours> openingHours = new HashSet<>();
		for(FetchedOpeningHours f : fetchedCanteen.getOpeningHours()){
			openingHours.add(
					new OpeningHours(
							canteen,
							dayOfWeekToWeekday(f.getWeekday()),
							f.isOpen(),
							f.getOpeningAt(),
							f.getClosingAt(),
							f.getGetAMealTill()
					));
		}
		canteen.setOpeningHours(openingHours);
		canteenRepository.save(canteen);

		return canteen;
	}

	private Weekday dayOfWeekToWeekday(DayOfWeek weekday){
		String weekdayName = switch (weekday){
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