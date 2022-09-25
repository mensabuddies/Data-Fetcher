package com.example.mensaapi.controller;

import com.example.mensaapi.ResponseHandler;
import com.example.mensaapi.database.entities.FoodProvider;
import com.example.mensaapi.database.entities.OpeningHours;
import com.example.mensaapi.database.repositories.FoodProviderRepository;
import com.example.mensaapi.database.repositories.FoodProviderTypeRepository;
import com.example.mensaapi.database.repositories.MenuRepository;
import com.example.mensaapi.database.repositories.OpeningHoursRepository;
import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/foodproviders")
public class FoodProviderController {
    @Autowired
    FoodProviderRepository foodProviderRepository;
    @Autowired
    OpeningHoursRepository openingHoursRepository;
    @Autowired
    MenuRepository menuRepository;

    @Autowired
    FoodProviderTypeRepository foodProviderTypeRepository;

    /**
     * Get all food providers.
     * @return
     */
    @GetMapping(value = "")
    public Object getFoodProviders() {
        List<FoodProvider> foodProviders = new ArrayList<>((Collection) foodProviderRepository.findAll());

        if(foodProviders.isEmpty()){
            return ResponseHandler.generateError(HttpStatus.INTERNAL_SERVER_ERROR, "No food providers found.");
        }

        JSONArray jA = new JSONArray();
        for(FoodProvider foodProvider : foodProviders){
            jA.add(JsonCreators.constructFoodProviderJsonObject(foodProvider));
        }

        return ResponseHandler.generateResponseWithoutMessage(HttpStatus.OK, jA);
    }

    /**
     * Get a food provider by its id.
     * @param id
     * @return
     */
    @GetMapping(value = "{id}")
    public ResponseEntity<Object> getCanteen(@PathVariable int id) {
        Optional<FoodProvider> canteen = foodProviderRepository.getFoodProviderById(id);

        if(canteen.isEmpty()){
            return ResponseHandler.generateError(HttpStatus.NOT_FOUND, "No canteen with this id.");
        }

        return ResponseHandler.generateResponseWithoutMessage(
                HttpStatus.OK,
                JsonCreators.constructFoodProviderJsonObject(canteen.get())
        );
    }


    /**
     * Get the opening hours for a specific food provider.
     * @param id
     * @return
     */
    @GetMapping(value = "{id}/openinghours")
    public ResponseEntity<Object> getOpeningHours(@PathVariable int id) {
        List<OpeningHours> openingHours = openingHoursRepository.getOpeningHoursForFoodProvider(id);

        if(openingHours.isEmpty()){
            return ResponseHandler.generateError(HttpStatus.NOT_FOUND, "No opening hours found for this id.");
        }

        return ResponseHandler.generateResponseWithoutMessage(HttpStatus.OK, JsonCreators.constructOpeningHoursJsonObject(openingHours));
    }
}