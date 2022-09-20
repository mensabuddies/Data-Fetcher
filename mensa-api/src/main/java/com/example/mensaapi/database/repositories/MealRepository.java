package com.example.mensaapi.database.repositories;

import com.example.mensaapi.database.entities.Meal;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MealRepository extends CrudRepository<Meal, Integer> {
    List<Meal> getMealByName(String name);
}