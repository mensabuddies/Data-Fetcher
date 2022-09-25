package com.example.mensaapi.controller;

import com.example.mensaapi.ResponseHandler;
import com.example.mensaapi.data_fetcher.dataclasses.enums.FetchedFoodProviderType;
import com.example.mensaapi.database.entities.FoodProvider;
import com.example.mensaapi.database.entities.FoodProviderType;
import com.example.mensaapi.database.entities.Meal;
import com.example.mensaapi.database.entities.Menu;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/canteens")
public class CanteenController {
    @Autowired
    FoodProviderRepository foodProviderRepository;
    @Autowired
    FoodProviderTypeRepository foodProviderTypeRepository;
    @Autowired
    OpeningHoursRepository openingHoursRepository;
    @Autowired
    MenuRepository menuRepository;

    /**
     * Get all canteens with their details and opening hours (but without meals).
     * @return
     */
    @GetMapping(value = "")
    public ResponseEntity<Object> getCanteens() {
        List<FoodProvider> canteens = foodProviderRepository.getFoodProviderByType(canteenType());

        if(canteens.isEmpty()){
            return ResponseHandler.generateError(HttpStatus.INTERNAL_SERVER_ERROR, "No canteens found.");
        }

        JSONArray jA = new JSONArray();
        for(FoodProvider foodProvider : canteens){
            jA.add(JsonCreators.constructFoodProviderJsonObject(foodProvider));
        }

        return ResponseHandler.generateResponseWithoutMessage(HttpStatus.OK, jA);
    }


    /**
     * Get the menus for a specify canteen for today and on.
     * @param id
     * @return
     */
    @GetMapping(value = "{id}/menus/today-and-beyond")
    public ResponseEntity<Object> getMenusOfCanteenStartingFromToday(@PathVariable int id) {
        List<Menu> menus = menuRepository.getMealsForCanteenFromTodayOn(id);

        if(menus.isEmpty()){
            return ResponseHandler.generateError(HttpStatus.NOT_FOUND, "There are no menus for this id.");
        }

        // Get a list with all dates
        Set<LocalDate> dates = new HashSet<>();
        for(Menu menu : menus){
            dates.add(menu.getDate());
        }

        // Group the entries by date
        JSONArray jA = new JSONArray();
        for(LocalDate date : dates){
            List<Menu> menusForToday = new ArrayList<>();
            for(Menu menu : menus){
                if(menu.getDate().equals(date)){
                    menusForToday.add(menu);
                }
            }
            jA.add(constructMenuJsonObject(menusForToday, date));
        }
        Collections.reverse(jA);

        return ResponseHandler.generateResponseWithoutMessage(HttpStatus.OK, jA);
    }


    /**
     * Get the menus for a specific canteen for today.
     * @param id
     * @return
     */
    @GetMapping(value = "{id}/menus/today")
    public ResponseEntity<Object> getTodaysMenu(@PathVariable int id) {
        List<Menu> menusForToday = menuRepository.findMenusByFoodProviderIdAndDate(id, LocalDate.now());

        if(menusForToday.isEmpty()){
            return ResponseHandler.generateError(HttpStatus.NOT_FOUND, "No menus for that canteen today.");
        }

        return ResponseHandler.generateResponseWithoutMessage(HttpStatus.OK, constructMenuJsonObject(menusForToday, LocalDate.now()));
    }

    /**
     * Helper function to create a JSON object for a menu.
     * @param menus
     * @param date
     * @return
     */
    private JSONObject constructMenuJsonObject(List<Menu> menus, LocalDate date) {
        JSONArray jA = new JSONArray();
        for (Menu menu : menus) {
            jA.add(constructMealJsonObject(menu.getMeal()));
        }

        JSONObject jO = new JSONObject();
        jO.put("date", date);
        jO.put("meals", jA);
        return jO;
    }

    /**
     * Helper function to create a JSON object for a meal.
     * @param meal
     * @return
     */
    private JSONObject constructMealJsonObject(Meal meal) {
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


    /**
     * Helper function to convert the prices stored as an int to Euro.
     * @param price
     * @return
     */
    private String mealPriceToString(int price) {
        DecimalFormat d = new DecimalFormat("0.00", new DecimalFormatSymbols(Locale.GERMANY));
        return d.format(price / 100.0) + " \u20ac";
    }


    /**
     * Helper method for getting a food provider object of type CANTEEN.
     * @return
     */
    private FoodProviderType canteenType(){
        return foodProviderTypeRepository.findByName(FetchedFoodProviderType.CANTEEN.getValue());
    }
}
