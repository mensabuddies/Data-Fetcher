package com.example.mensaapi.database.repositories;

import com.example.mensaapi.database.entities.Canteen;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CanteenRepository extends CrudRepository<Canteen, Integer> {
    Optional<Canteen> getCanteenByName(String name);
}