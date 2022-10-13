package com.example.mensaapi.database.repositories;

import com.example.mensaapi.database.entities.MealComponent;
import com.example.mensaapi.database.entities.MealComponentType;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface MealComponentRepository extends CrudRepository<MealComponent, Integer> {
    Optional<MealComponent> getMealComponentByName(String name);

    List<MealComponent> getMealComponentByType(MealComponentType type);

    Optional<MealComponent> getMealComponentById(int id);
}
