package com.example.mensaapi.database.repositories;

import com.example.mensaapi.database.entities.Weekday;
import org.springframework.data.repository.CrudRepository;

import java.time.DayOfWeek;

public interface WeekdayRepository extends CrudRepository<Weekday, Integer> {
    Weekday getWeekdayByName(String weekdayName);
}