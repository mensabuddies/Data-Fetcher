package com.example.mensaapi.database.repositories;

import com.example.mensaapi.database.entities.Meal;
import org.springframework.data.repository.CrudRepository;

public interface MealRepository extends CrudRepository<Meal, Integer> {
    Meal getMealByName(String name);
}