package com.example.mensaapi;

import com.example.mensaapi.data_fetcher.dataclasses.enums.FetchedFoodProviderType;
import com.example.mensaapi.database.entities.FoodProvider;
import com.example.mensaapi.database.entities.Meal;
import com.example.mensaapi.database.entities.Menu;
import com.example.mensaapi.database.repositories.FoodProviderRepository;
import com.example.mensaapi.database.repositories.FoodProviderTypeRepository;
import com.example.mensaapi.database.repositories.MealRepository;
import com.example.mensaapi.database.repositories.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    @Autowired
    FoodProviderRepository foodProviderRepository;
    @Autowired
    MenuRepository menuRepository;

    @Autowired
    MealRepository mealRepository;

    @Autowired
    FoodProviderTypeRepository foodProviderTypeRepository;

    @GetMapping(value = "/canteens")
    public ResponseEntity<Object> getCanteens() {
        try {
            List<FoodProvider> foodProviders = new ArrayList<>();

            foodProviderRepository.getFoodProvidersByType(foodProviderTypeRepository.findByName(FetchedFoodProviderType.CANTEEN.getValue()))
                    .orElseThrow().iterator().forEachRemaining(foodProviders::add);

            return ResponseHandler.generateResponse("Test", HttpStatus.OK, foodProviders);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.MULTI_STATUS, null);
        }
    }

    @GetMapping(value = "/canteens/{id}")
    public ResponseEntity<Object> getCanteen(@PathVariable int id) {
        FoodProvider c = foodProviderRepository.findById(id).orElse(null);

        if (c != null)
            return ResponseHandler.generateResponse("Test", HttpStatus.OK, c);
        else
            return ResponseHandler.generateResponse("Canteen not found", HttpStatus.NOT_FOUND, null);
    }

    @GetMapping(value = "/canteens/{id}/openinghours")
    public ResponseEntity<Object> getOpeningHours(@PathVariable int id) {
        FoodProvider c = foodProviderRepository.findById(id).orElse(null);

        if (c != null)
            return ResponseHandler.generateResponse("Test", HttpStatus.OK, c.getOpeningHours());
        else
            return ResponseHandler.generateResponse("Canteen not found", HttpStatus.NOT_FOUND, null);
    }

    @GetMapping(value = "/canteens/{id}/menus")
    public ResponseEntity<Object> getMenusOfCanteen(@PathVariable int id) {
        FoodProvider c = foodProviderRepository.findById(id).orElse(null);

        if (c != null)
            return ResponseHandler.generateResponse("Test", HttpStatus.OK, c.getMenus());
        else
            return ResponseHandler.generateResponse("Canteen not found", HttpStatus.NOT_FOUND, null);
    }

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