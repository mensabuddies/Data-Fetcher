package com.example.mensaapi.database.repositories;

import com.example.mensaapi.database.entities.FoodProvider;
import com.example.mensaapi.database.entities.FoodProviderType;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface FoodProviderRepository extends CrudRepository<FoodProvider, Integer> {
    Optional<FoodProvider> getFoodProviderByName(String name);

    Optional<List<FoodProvider>> getFoodProvidersByType(FoodProviderType type);
}