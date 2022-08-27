package com.example.mensaapi.database.repositories;

import com.example.mensaapi.database.entities.Canteen;
import org.springframework.data.repository.CrudRepository;

public interface CanteenRepository extends CrudRepository<Canteen, Integer> {
}