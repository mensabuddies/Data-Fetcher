package com.example.mensaapi.database.repositories;

import com.example.mensaapi.database.entities.FoodProvider;
import com.example.mensaapi.database.entities.OpeningHours;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface OpeningHoursRepository extends CrudRepository<OpeningHours, Integer> {
    Optional<List<OpeningHours>> findByFoodProviderIdEqualsOrderByWeekday(Integer canteenId);

    @Transactional
    List<OpeningHours> deleteByFoodProviderId(Integer foodProviderId);

    List<OpeningHours> findByFoodProvider(FoodProvider foodProvider);

    @Query(value = "select * from opening_hours join weekdays w on w.weekday_id = opening_hours.weekday_id where food_provider_id = :id", nativeQuery = true)
    List<OpeningHours> getOpeningHoursForFoodProvider(@Param("id") int id);
}