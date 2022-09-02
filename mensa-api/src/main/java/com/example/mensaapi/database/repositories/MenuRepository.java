package com.example.mensaapi.database.repositories;

import com.example.mensaapi.database.entities.Menu;
import org.springframework.data.repository.CrudRepository;

public interface MenuRepository extends CrudRepository<Menu, Integer> {
}