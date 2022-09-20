package com.example.mensaapi.controller;

import com.example.mensaapi.ResponseHandler;
import com.example.mensaapi.database.entities.Canteen;
import com.example.mensaapi.database.entities.Meal;
import com.example.mensaapi.database.entities.Menu;
import com.example.mensaapi.database.entities.OpeningHours;
import com.example.mensaapi.database.repositories.CanteenRepository;
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

@RestController
public class CanteenController {
    @Autowired CanteenRepository canteenRepository;
    @Autowired OpeningHoursRepository openingHoursRepository;
    @Autowired MenuRepository menuRepository;

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
        Set<OpeningHours> openingHours = openingHoursRepository.findOpeningHoursByCanteen_Id(id);

        if (openingHours.isEmpty()){
            return ResponseHandler.generateResponse("Canteen not found", HttpStatus.NOT_FOUND, null);
        }

        return ResponseHandler.generateResponse(HttpStatus.OK, constructOpeningHoursJsonObject(openingHours));
    }

    @GetMapping(value = "/canteens/{id}/menus")
    public List<Menu> getMenusOfCanteen(@PathVariable int id) {
        List<Menu> menus = menuRepository.findMenuByCanteen_IdOrderByDate(id);

        Set<LocalDate> dates = new HashSet<>();
        for(Menu menu : menus){
            dates.add(menu.getDate());
        }

        JSONArray jA = new JSONArray();
        for(LocalDate date : dates){
            List<Menu> menusForToday = menuRepository.findMenuByCanteen_IdAndDate(id, date);
            jA.add(constructMenuJsonObject(menusForToday, date));
        }
        Collections.reverse(jA);

       return jA;
    }

    @GetMapping(value="/canteens/{id}/menus/today")
    public Object getTodaysMenu(@PathVariable int id){
        List<Menu> menus = menuRepository.findMenuByCanteen_IdAndDate(id, LocalDate.now());

        return constructMenuJsonObject(menus, LocalDate.now());
    }

    private JSONObject constructMenuJsonObject(List<Menu> menus, LocalDate date){
        JSONArray jA = new JSONArray();
        for(Menu menu : menus){
            jA.add(constructMealJsonObject(menu.getMeal()));
        }

        JSONObject jO = new JSONObject();
        jO.put(date, jA);
        return jO;
    }

    private JSONObject constructMealJsonObject(Meal meal){
        JSONObject jO = new JSONObject();
        jO.put("id", meal.getId());
        jO.put("name", meal.getName());
        jO.put("priceStudent", mealPriceToString(meal.getPriceStudent()));
        jO.put("priceEmployee", mealPriceToString(meal.getPriceEmployee()));
        jO.put("priceGuest", mealPriceToString(meal.getPriceGuest()));
        jO.put("allergens", meal.getAllergens());
        jO.put("ingredients", meal.getIngredients());

        return jO;
    }

    private String mealPriceToString(int price){
        DecimalFormat d = new DecimalFormat("0.00", new DecimalFormatSymbols(Locale.GERMANY));
        return d.format(price / 100.0) + " \u20ac";
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