package com.example.mensaapi.controller;

import com.example.mensaapi.ResponseHandler;
import com.example.mensaapi.data_fetcher.dataclasses.enums.MealComponentTypeEnum;
import com.example.mensaapi.database.entities.MealComponent;
import com.example.mensaapi.database.entities.MealComponentType;
import com.example.mensaapi.database.repositories.MealComponentRepository;
import com.example.mensaapi.database.repositories.MealComponentTypeRepository;
import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/mealcomponents")
public class MealComponentController {
    @Autowired
    MealComponentRepository mealComponentRepository;

    @Autowired
    MealComponentTypeRepository mealComponentTypeRepository;

    @GetMapping("")
    public Object getMealComponents() {
        List<MealComponent> mealComponents = new ArrayList<>();
        mealComponentRepository.findAll().forEach(mealComponents::add);


        if (mealComponents.isEmpty()) {
            return ResponseHandler.generateError(HttpStatus.INTERNAL_SERVER_ERROR, "No meal components found");
        }

        JSONArray jA = new JSONArray();
        for (MealComponent mc: mealComponents) {
            jA.add(JsonCreators.constructMealComponentJsonObject(mc, mc.getType()));
        }
        return ResponseHandler.generateResponseWithoutMessage(HttpStatus.OK, jA);
    }

    @GetMapping("/ingredients")
    public Object getIngredients() {
        List<MealComponent> ingredients =
                new ArrayList<>(
                        mealComponentRepository.getMealComponentByType(getInstanceOf(MealComponentTypeEnum.INGREDIENT))
                );

        if (ingredients.isEmpty()) {
            return ResponseHandler.generateError(HttpStatus.INTERNAL_SERVER_ERROR, "No ingredients found");
        }

        JSONArray jA = new JSONArray();
        for (MealComponent ingredient: ingredients) {
            jA.add(JsonCreators.constructMealComponentJsonObject(ingredient));
        }
        return ResponseHandler.generateResponseWithoutMessage(HttpStatus.OK, jA);
    }

    @GetMapping("/allergens")
    public Object getAllergens() {
        List<MealComponent> allergens =
                new ArrayList<>(
                        mealComponentRepository.getMealComponentByType(getInstanceOf(MealComponentTypeEnum.ALLERGEN))
                );

        if (allergens.isEmpty()) {
            return ResponseHandler.generateError(HttpStatus.INTERNAL_SERVER_ERROR, "No allergens found");
        }

        JSONArray jA = new JSONArray();
        for (MealComponent ingredient: allergens) {
            jA.add(JsonCreators.constructMealComponentJsonObject(ingredient));
        }
        return ResponseHandler.generateResponseWithoutMessage(HttpStatus.OK, jA);
    }

    /**
     * Helper method for getting a food provider object of type CANTEEN.
     *
     * @return
     */
    private MealComponentType getInstanceOf(MealComponentTypeEnum mcType) {
        return mealComponentTypeRepository.findByName(mcType.getValue());
    }
}
