package com.example.mensaapi.controller;

import com.example.mensaapi.ResponseHandler;
import com.example.mensaapi.database.entities.Meal;
import com.example.mensaapi.database.entities.Menu;
import com.example.mensaapi.database.repositories.MealRepository;
import com.example.mensaapi.database.repositories.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class FoodController {
    @Autowired MenuRepository menuRepository;
    @Autowired MealRepository mealRepository;

    @GetMapping(value = "/menus")
    public ResponseEntity<Object> getMenus() {
        List<Menu> menus = new ArrayList<>();
        menuRepository.findAll().forEach(menus::add);

        if (!menus.isEmpty()) {
            return ResponseHandler.generateResponse("Test", HttpStatus.OK, menus);
        } else {
            return ResponseHandler.generateResponse("No menus found", HttpStatus.NOT_FOUND, null);
        }
    }

    @GetMapping(value = "/meals")
    public ResponseEntity<Object> getMeals() {
        List<Meal> meals = new ArrayList<>();
        mealRepository.findAll().forEach(meals::add);

        if (!meals.isEmpty()) {
            return ResponseHandler.generateResponse("Test", HttpStatus.OK, meals);
        } else {
            return ResponseHandler.generateResponse("No menus found", HttpStatus.NOT_FOUND, null);
        }
    }
}
