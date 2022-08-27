package com.example.mensaapi;

import com.example.mensaapi.database.entities.Weekday;
import com.example.mensaapi.database.repositories.WeekdayRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MensaApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MensaApiApplication.class, args);
	}

	/*@Bean
	public CommandLineRunner run(WeekdayRepository repository){
		return (args -> {
			insertWeekdays(repository);
			System.out.println(repository.findAll());
		});
	}*/

	private void insertWeekdays(WeekdayRepository repository){
		String[] weekdays = {"Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag", "Sonntag"};
		for(String day : weekdays){
			repository.save(new Weekday(day));
		}
	}
}
