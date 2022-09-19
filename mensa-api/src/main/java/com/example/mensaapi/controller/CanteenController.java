package com.example.mensaapi.controller;

import com.example.mensaapi.ResponseHandler;
import com.example.mensaapi.database.entities.Canteen;
import com.example.mensaapi.database.entities.OpeningHours;
import com.example.mensaapi.database.repositories.CanteenRepository;
import com.example.mensaapi.database.repositories.OpeningHoursRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
public class CanteenController {
    @Autowired CanteenRepository canteenRepository;
    @Autowired OpeningHoursRepository openingHoursRepository;

    @GetMapping(value = "/canteens")
    public JSONArray getCanteens() {
        List<Canteen> canteens = canteenRepository.findAll();
        JSONArray canteensArray = new JSONArray();

        for(Canteen currentCanteen : canteens){
            canteensArray.add(constructCanteenJsonObject(currentCanteen));
        }
        return canteensArray;
    }

    @GetMapping(value = "/canteens/{id}")
    public ResponseEntity<Object> getCanteen(@PathVariable int id) {
        Canteen c = canteenRepository.findById(id).orElse(null);

        if (c == null) {
            return ResponseHandler.generateResponse("Canteen not found", HttpStatus.NOT_FOUND, null);
        }

        return ResponseHandler.generateResponse(HttpStatus.OK, constructCanteenJsonObject(c));
    }

    @GetMapping(value = "/canteens/{id}/openinghours")
    public ResponseEntity<Object> getOpeningHours(@PathVariable int id) {
        Optional<Canteen> c = canteenRepository.findById(id);
        if (!c.isPresent()){
            return ResponseHandler.generateResponse("Canteen not found", HttpStatus.NOT_FOUND, null);
        }

        Set<OpeningHours> openingHours = openingHoursRepository.findOpeningHoursByCanteen(c.get());

        return ResponseHandler.generateResponse(HttpStatus.OK, constructOpeningHoursJsonObject(openingHours));
    }

    @GetMapping(value = "/canteens/{id}/menus")
    public ResponseEntity<Object> getMenusOfCanteen(@PathVariable int id) {
        Canteen c = canteenRepository.findById(id).orElse(null);

        if (c != null)
            return ResponseHandler.generateResponse("Test", HttpStatus.OK, c.getMenus());
        else
            return ResponseHandler.generateResponse("Canteen not found", HttpStatus.NOT_FOUND, null);
    }

    private JSONObject constructCanteenJsonObject(Canteen canteen){
        JSONObject jO = new JSONObject();
        jO.put("id", canteen.getId());
        jO.put("name", canteen.getName());
        jO.put("location", canteen.getLocation().getName());
        jO.put("openingHours", constructOpeningHoursJsonObject(canteen.getOpeningHours()));
        jO.put("info", canteen.getInfo());
        jO.put("additionalInfo", canteen.getAdditionalInfo());
        jO.put("linkToFoodPlan", canteen.getLinkToFoodPlan());

        return jO;
    }

    private JSONArray constructOpeningHoursJsonObject(Set<OpeningHours> openingHours){
        JSONArray jA = new JSONArray();
        for(OpeningHours currentOpeningHours : openingHours){
            JSONObject jO = new JSONObject();

            jO.put("weekday", currentOpeningHours.getWeekday().getName());
            jO.put("opensAt", currentOpeningHours.getOpensAt());
            jO.put("closesAt", currentOpeningHours.getClosesAt());
            jO.put("isOpened", currentOpeningHours.isOpened());

            jA.add(jO);
        }

        return jA;
    }
}