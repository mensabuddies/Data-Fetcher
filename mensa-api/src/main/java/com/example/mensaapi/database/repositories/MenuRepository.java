package com.example.mensaapi.database.repositories;

import com.example.mensaapi.database.entities.Meal;
import com.example.mensaapi.database.entities.Menu;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface MenuRepository extends CrudRepository<Menu, Integer> {
    Optional<Menu> findMenuByDateAndMeal(LocalDate data, Meal meal);
}