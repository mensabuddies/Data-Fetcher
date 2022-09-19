package com.example.mensaapi.database.repositories;

import com.example.mensaapi.database.entities.Canteen;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CanteenRepository extends JpaRepository<Canteen, Integer> {
    Optional<Canteen> getCanteenByName(String name);
}