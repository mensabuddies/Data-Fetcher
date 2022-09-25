package com.example.mensaapi.database.repositories;

import com.example.mensaapi.database.entities.Meal;
import com.example.mensaapi.database.entities.Menu;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

public interface MenuRepository extends CrudRepository<Menu, Integer> {

    Optional<Menu> findByDateAndMeal(LocalDate data, Meal meal);

    List<Menu> findMenuByFoodProviderIdAndDate(int foodProviderId, LocalDate date);

    Optional<List<Menu>> findByFoodProviderIdEqualsOrderByDate(int foodProviderId);

    Optional<List<Menu>> findByFoodProviderIdEqualsAndDate(int foodProviderId, LocalDate date);
    Optional<Menu> findMenuByDateAndMealAndFoodProviderId(LocalDate data, Meal meal, int foodProviderId);

    @Query(value="select * from menus join meals m on m.meal_id = menus.meal_id where date >= CURDATE() AND food_provider_id = :id order by date;", nativeQuery = true)
    List<Menu> getMealsForCanteenFromTodayOn(@Param("id") int id);
}