package com.example.mensaapi.database.repositories;

import com.example.mensaapi.database.entities.MealComponentType;
import org.springframework.data.repository.CrudRepository;

public interface MealComponentTypeRepository extends CrudRepository<MealComponentType, Integer> {
    MealComponentType findByName(String name);
}
