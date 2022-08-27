package com.example.mensaapi.database.repositories;

import com.example.mensaapi.database.entities.Ingredients;
import org.springframework.data.repository.CrudRepository;

public interface IngredientsRepository extends CrudRepository<Ingredients, Integer> {
}