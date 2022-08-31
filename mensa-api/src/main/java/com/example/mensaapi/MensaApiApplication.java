package com.example.mensaapi;

import com.example.mensaapi.data_fetcher.DataFetcher;
import com.example.mensaapi.data_fetcher.dataclasses.interfaces.FetchedCanteen;
import com.example.mensaapi.database.entities.Location;
import com.example.mensaapi.database.entities.Weekday;
import com.example.mensaapi.database.repositories.CanteenRepository;
import com.example.mensaapi.database.repositories.LocationRepository;
import com.example.mensaapi.database.repositories.WeekdayRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class MensaApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MensaApiApplication.class, args);
	}

	@Bean
	public CommandLineRunner run(WeekdayRepository weekdayRepository, LocationRepository locationRepository){
		return (args -> {
			insertWeekdays(weekdayRepository);
			insertLocations(locationRepository);
		});
	}

	@Bean
	public CommandLineRunner run(CanteenRepository canteenRepository){
		return (args -> storeStudentenwerkDataInDatabase(canteenRepository));
	}


	private void storeStudentenwerkDataInDatabase(CanteenRepository canteenRepository){
		List<FetchedCanteen> fetchedFetchedCanteens = new DataFetcher().get();
		for(FetchedCanteen fetchedCanteen : fetchedFetchedCanteens){
			com.example.mensaapi.database.entities.Canteen canteen = new com.example.mensaapi.database.entities.Canteen();
			canteen.setName(fetchedCanteen.getName());

		}
	}

	private void insertLocations(LocationRepository repository){
		String[] locations = {"Würzburg", "Schweinfurt", "Bamberg", "Aschaffenburg"};
		for(String location : locations){
			repository.save(new Location(location));
		}
	}
	private void insertWeekdays(WeekdayRepository repository){
		String[] weekdays = {"Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag", "Sonntag"};
		for(String day : weekdays){
			repository.save(new Weekday(day));
		}
	}
}
