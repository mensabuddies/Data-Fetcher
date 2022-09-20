package com.example.mensaapi.database.repositories;

import com.example.mensaapi.database.entities.FoodProviderType;
import org.springframework.data.repository.CrudRepository;

public interface FoodProviderTypeRepository extends CrudRepository<FoodProviderType, Integer> {

    FoodProviderType findByName(String name);
}
