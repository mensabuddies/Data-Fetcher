package com.example.mensaapi.database.repositories;

import com.example.mensaapi.database.entities.Meal;
import com.example.mensaapi.database.entities.Menu;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

public interface MenuRepository extends CrudRepository<Menu, Integer> {

    Optional<Menu> findByDateAndMeal(LocalDate data, Meal meal);

    //Optional<Menu> findMenuByFoodProviderIdAndDate(int foodProviderId, LocalDate date);

    Optional<List<Menu>> findByFoodProviderIdEqualsOrderByDate(int foodProviderId);

    Optional<List<Menu>> findByFoodProviderIdEqualsAndDate(int foodProviderId, LocalDate date);
    Optional<Menu> findMenuByDateAndMealAndFoodProviderId(LocalDate data, Meal meal, int foodProviderId);
}