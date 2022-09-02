package com.example.mensaapi;

import com.example.mensaapi.data_fetcher.DataFetcher;
import com.example.mensaapi.data_fetcher.dataclasses.FetchedDay;
import com.example.mensaapi.data_fetcher.dataclasses.interfaces.FetchedCanteen;
import com.example.mensaapi.data_fetcher.dataclasses.interfaces.FetchedMeal;
import com.example.mensaapi.data_fetcher.dataclasses.interfaces.FetchedOpeningHours;
import com.example.mensaapi.database.Util;
import com.example.mensaapi.database.entities.*;
import com.example.mensaapi.database.repositories.CanteenRepository;
import com.example.mensaapi.database.repositories.LocationRepository;
import com.example.mensaapi.database.repositories.WeekdayRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.DayOfWeek;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootApplication
public class MensaApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MensaApiApplication.class, args);
	}

	@Bean
	public CommandLineRunner run(WeekdayRepository weekdayRepository, LocationRepository locationRepository, CanteenRepository canteenRepository){
		return (args -> {
			Util u = new Util();
			u.insertWeekdays(weekdayRepository);
			u.insertLocations(locationRepository);

			storeStudentenwerkDataInDatabase(canteenRepository, weekdayRepository, locationRepository);
		});
	}


	private void storeStudentenwerkDataInDatabase(CanteenRepository canteenRepository, WeekdayRepository weekdayRepository, LocationRepository locationRepository){
		List<FetchedCanteen> fetchedCanteens = new DataFetcher().get();

		for(FetchedCanteen fetchedCanteen : fetchedCanteens){
			// First, we store the canteen with its details (like opening hours, etc.)
			Canteen canteen = storeCanteen(fetchedCanteen, canteenRepository, weekdayRepository, locationRepository);

			// Then, we store the meals
			for(FetchedDay mealsForTheDay : fetchedCanteen.getMenus()){
				for(FetchedMeal fetchedMeal : mealsForTheDay.getMeals()){
					storeMeal(fetchedMeal);
				}
			}

			// TODO: Link meals to canteens via menus
		}
	}

	private Meal storeMeal(FetchedMeal fetchedMeal){
		// TODO
		return null;
	}

	private Canteen storeCanteen(FetchedCanteen fetchedCanteen, CanteenRepository canteenRepository, WeekdayRepository weekdayRepository, LocationRepository locationRepository){
		Canteen canteen = new Canteen();

		canteen.setName(fetchedCanteen.getName());
		canteen.setLocation(locationRepository.getLocationByName(fetchedCanteen.getLocation().getValue()));
		canteen.setInfo(fetchedCanteen.getTitleInfo());
		canteen.setAdditionalInfo(fetchedCanteen.getBodyInfo());
		canteen.setLinkToFoodPlan(fetchedCanteen.getLinkToFoodPlan());

		canteenRepository.save(canteen);

		Set<OpeningHours> openingHours = new HashSet<>();
		for(FetchedOpeningHours f : fetchedCanteen.getOpeningHours()){
			openingHours.add(
					new OpeningHours(
							canteen,
							dayOfWeekToWeekday(weekdayRepository, f.getWeekday()),
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

	private Weekday dayOfWeekToWeekday(WeekdayRepository weekdayRepository, DayOfWeek weekday){
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
