package com.example.mensaapi.controller;

import com.example.mensaapi.ResponseHandler;
import com.example.mensaapi.data_fetcher.dataclasses.enums.FetchedFoodProviderType;
import com.example.mensaapi.database.entities.FoodProvider;
import com.example.mensaapi.database.entities.Meal;
import com.example.mensaapi.database.entities.Menu;
import com.example.mensaapi.database.entities.OpeningHours;
import com.example.mensaapi.database.repositories.FoodProviderRepository;
import com.example.mensaapi.database.repositories.FoodProviderTypeRepository;
import com.example.mensaapi.database.repositories.MenuRepository;
import com.example.mensaapi.database.repositories.OpeningHoursRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@RestController
public class FoodProviderController {
    @Autowired
    FoodProviderRepository foodProviderRepository;
    @Autowired
    OpeningHoursRepository openingHoursRepository;
    @Autowired
    MenuRepository menuRepository;

    @Autowired
    FoodProviderTypeRepository foodProviderTypeRepository;

    /*
    @GetMapping(value = "/cafeterias")
    public ResponseEntity<Object> getCafeterias() {
        try {
            List<FoodProvider> cafeterias = new ArrayList<>();

            foodProviderRepository.getFoodProvidersByType(foodProviderTypeRepository.findByName(FetchedFoodProviderType.CAFETERIA.getValue()))
                    .orElseThrow().iterator().forEachRemaining(cafeterias::add);
            JSONArray cafeteriasArray = new JSONArray();
            for (FoodProvider currentCafeteria : cafeterias) {
                cafeteriasArray.add(constructCanteenJsonObject(currentCafeteria));
            }
            return ResponseHandler.generateResponse("Test", HttpStatus.OK, cafeteriasArray);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.MULTI_STATUS, null);
        }
    }

    @GetMapping(value = "/food_providers")
    public ResponseEntity<Object> getFoodProviders() {
        try {
            List<FoodProvider> foodProviders = new ArrayList<>();

            foodProviderRepository.findAll().iterator().forEachRemaining(foodProviders::add);
            JSONArray foodProvidersArray = new JSONArray();
            for (FoodProvider currentFoodProvider : foodProviders) {
                foodProvidersArray.add(constructCanteenJsonObject(currentFoodProvider));
            }
            return ResponseHandler.generateResponse("Test", HttpStatus.OK, foodProvidersArray);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.MULTI_STATUS, null);
        }
    }

    @GetMapping(value = "/cafeteria/{id}")
    public ResponseEntity<Object> getCafeteria(@PathVariable int id) {
        FoodProvider c = foodProviderRepository.findById(id).orElse(null);

        if (c != null && c.getType().getName().equals(FetchedFoodProviderType.CAFETERIA.getValue()))
            return ResponseHandler.generateResponse("Test", HttpStatus.OK, c);
        else
            return ResponseHandler.generateResponse("Canteen not found", HttpStatus.NOT_FOUND, null);
    }

    @GetMapping(value = "/cafeteria/{id}/openinghours")
    public ResponseEntity<Object> getOpeningHoursCafe(@PathVariable int id) {
        FoodProvider c = foodProviderRepository.findById(id).orElse(null);

        if (c != null && c.getType().getName().equals(FetchedFoodProviderType.CAFETERIA.getValue()))
            return ResponseHandler.generateResponse("Test", HttpStatus.OK, c.getOpeningHours());
        else
            return ResponseHandler.generateResponse("Canteen not found", HttpStatus.NOT_FOUND, null);
    }

     */
}