package com.example.mensaapi.database.repositories;

import com.example.mensaapi.database.entities.Meal;
import com.example.mensaapi.database.entities.Menu;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MenuRepository extends CrudRepository<Menu, Integer> {

    List<Menu> findMenusByFoodProviderIdAndDate(int foodProviderId, LocalDate date);

    Optional<Menu> findMenuByDateAndMealAndFoodProviderId(LocalDate data, Meal meal, int foodProviderId);

    @Query(value="select * from menus join meals m on m.meal_id = menus.meal_id where date >= CURDATE() AND food_provider_id = :id order by date;", nativeQuery = true)
    List<Menu> getMealsForCanteenFromTodayOn(@Param("id") int id);
}