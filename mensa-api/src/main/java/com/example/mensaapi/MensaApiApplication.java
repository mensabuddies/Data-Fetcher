package com.example.mensaapi;

import com.example.mensaapi.data_fetcher.dataclasses.interfaces.Canteen;
import com.example.mensaapi.data_fetcher.retrieval.interfaces.Fetcher;
import com.example.mensaapi.data_fetcher.retrieval.interfaces.Parser;
import com.example.mensaapi.database.entities.Location;
import com.example.mensaapi.database.entities.Meal;
import com.example.mensaapi.database.entities.Weekday;
import com.example.mensaapi.database.repositories.CanteenRepository;
import com.example.mensaapi.database.repositories.LocationRepository;
import com.example.mensaapi.database.repositories.WeekdayRepository;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
		List<Canteen> fetchedCanteens = getDataFromStudentenwerk();
		for(Canteen fetchedCanteen : fetchedCanteens){
			com.example.mensaapi.database.entities.Canteen canteen = new com.example.mensaapi.database.entities.Canteen();
			canteen.setName(fetchedCanteen.getName());
		}
	}

	/**
	 * Gets the data from the homepage of the studentenwerk and updates the database
	 */
	private List<Canteen> getDataFromStudentenwerk(){
		Fetcher fetcher = Fetcher.createJSOUPFetcher("https://www.studentenwerk-wuerzburg.de/wuerzburg/essen-trinken/speiseplaene.html");
		Optional<Document> doc = fetcher.fetchCurrentData();

		List<Canteen> canteens = new ArrayList<>();
		Parser<Canteen> canteenParser = Parser.createCanteenParser();

		doc.ifPresent(document -> {
			Elements mensen = document.getElementsByClass("mensa");
			for (Element mensa: mensen) {
				canteens.add(canteenParser.parse(mensa).orElseThrow());
			}
		});
		return canteens;
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
