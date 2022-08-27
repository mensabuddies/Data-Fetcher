package com.example.mensaapi.database.repositories;

import com.example.mensaapi.database.entities.Location;
import org.springframework.data.repository.CrudRepository;

public interface LocationRepository extends CrudRepository<Location, Integer> {
}