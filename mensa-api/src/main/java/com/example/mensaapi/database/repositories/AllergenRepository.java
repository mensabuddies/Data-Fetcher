package com.example.mensaapi.database.repositories;

import com.example.mensaapi.database.entities.Allergen;
import org.springframework.data.repository.CrudRepository;

public interface AllergenRepository extends CrudRepository<Allergen, Integer> {
}