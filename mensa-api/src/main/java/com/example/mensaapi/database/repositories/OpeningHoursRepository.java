package com.example.mensaapi.database.repositories;

import com.example.mensaapi.database.entities.Canteen;
import com.example.mensaapi.database.entities.OpeningHours;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface OpeningHoursRepository extends CrudRepository<OpeningHours, Integer> {
    Set<OpeningHours> findOpeningHoursByCanteen_Id(int id);
}