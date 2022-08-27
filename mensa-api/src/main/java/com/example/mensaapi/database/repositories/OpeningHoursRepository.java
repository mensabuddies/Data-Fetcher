package com.example.mensaapi.database.repositories;

import com.example.mensaapi.database.entities.OpeningHours;
import org.springframework.data.repository.CrudRepository;

public interface OpeningHoursRepository extends CrudRepository<OpeningHours, Integer> {
}