package com.example.mensaapi.controller;

import com.example.mensaapi.ResponseHandler;
import com.example.mensaapi.data_fetcher.dataclasses.enums.FetchedFoodProviderType;
import com.example.mensaapi.database.entities.FoodProvider;
import com.example.mensaapi.database.entities.FoodProviderType;
import com.example.mensaapi.database.repositories.FoodProviderRepository;
import com.example.mensaapi.database.repositories.FoodProviderTypeRepository;
import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cafeterias")
public class CafeteriaController {
    @Autowired
    FoodProviderRepository foodProviderRepository;
    @Autowired
    FoodProviderTypeRepository foodProviderTypeRepository;

    /**
     * Get all cafeterias.
     * @return
     */
    @GetMapping(value = "")
    public ResponseEntity<Object> getCafeterias() {
        List<FoodProvider> cafeterias = foodProviderRepository.getFoodProviderByType(cafeteriaType());

        if(cafeterias.isEmpty()){
            return ResponseHandler.generateError(HttpStatus.INTERNAL_SERVER_ERROR, "No cafeterias found.");
        }

        JSONArray jA = new JSONArray();
        for(FoodProvider foodProvider : cafeterias){
            jA.add(JsonCreators.constructFoodProviderJsonObject(foodProvider));
        }

        return ResponseHandler.generateResponseWithoutMessage(HttpStatus.OK, jA);
    }


    /**
     * Helper method for getting a food provider object of type CAFETERIA.
     * @return
     */
    private FoodProviderType cafeteriaType(){
        return foodProviderTypeRepository.findByName(FetchedFoodProviderType.CAFETERIA.getValue());
    }
}
