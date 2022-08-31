package com.example.mensaapi.database;

import com.example.mensaapi.database.entities.Location;
import com.example.mensaapi.database.entities.Weekday;
import com.example.mensaapi.database.repositories.LocationRepository;
import com.example.mensaapi.database.repositories.WeekdayRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

public class Util {
    public void insertLocations(LocationRepository repository){
        String[] locations = {"Würzburg", "Schweinfurt", "Bamberg", "Aschaffenburg"};
        for(String location : locations){
            repository.save(new Location(location));
        }
    }
    public void insertWeekdays(WeekdayRepository repository){
        String[] weekdays = {"Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag", "Sonntag"};
        for(String day : weekdays){
            repository.save(new Weekday(day));
        }
    }
}
