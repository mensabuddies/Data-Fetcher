package com.example.mensaapi.database.repositories;

import com.example.mensaapi.database.entities.Menu;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

public interface MenuRepository extends CrudRepository<Menu, Integer> {
    List<Menu> findMenuByCanteen_IdOrderByDate(int id);
    List<Menu> findMenuByCanteen_IdAndDate(int id, LocalDate date);
}