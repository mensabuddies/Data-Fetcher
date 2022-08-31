package com.example.mensaapi;

import com.example.mensaapi.data_fetcher.DataFetcher;
import com.example.mensaapi.data_fetcher.dataclasses.interfaces.FetchedCanteen;
import com.example.mensaapi.database.Util;
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
	public CommandLineRunner run(WeekdayRepository weekdayRepository, LocationRepository locationRepository, CanteenRepository canteenRepository){
		return (args -> {
			Util u = new Util();
			u.insertWeekdays(weekdayRepository);
			u.insertLocations(locationRepository);

			storeStudentenwerkDataInDatabase(canteenRepository);
		});
	}


	private void storeStudentenwerkDataInDatabase(CanteenRepository canteenRepository){
		List<FetchedCanteen> fetchedFetchedCanteens = new DataFetcher().get();
		for(FetchedCanteen fetchedCanteen : fetchedFetchedCanteens){
			com.example.mensaapi.database.entities.Canteen canteen = new com.example.mensaapi.database.entities.Canteen();
			canteen.setName(fetchedCanteen.getName());

		}
	}
}
