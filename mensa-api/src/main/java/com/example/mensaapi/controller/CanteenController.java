package com.example.mensaapi.controller;

import com.example.mensaapi.ResponseHandler;
import com.example.mensaapi.data_fetcher.dataclasses.enums.FetchedFoodProviderType;
import com.example.mensaapi.database.entities.*;
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

import javax.swing.text.html.Option;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

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
     * Returns a list of all canteens with their details and opening hours (but without meals).
     * @return
     */
    @GetMapping(value = "/")
    public ResponseEntity<Object> getCanteens() {
        Optional<List<FoodProvider>> canteens = foodProviderRepository.getFoodProviderByType(canteenType());

        if(canteens.isEmpty()){
            return ResponseHandler.generateError(HttpStatus.INTERNAL_SERVER_ERROR, "No canteens found");
        }

        JSONArray jA = new JSONArray();
        for(FoodProvider foodProvider : canteens.get()){
            jA.add(constructCanteenJsonObject(foodProvider));
        }

        return ResponseHandler.generateResponseWithoutMessage(HttpStatus.OK, jA);
    }


    /**
     * Returns a canteen by its id
     * @param id
     * @return
     */
    @GetMapping(value = "{id}")
    public ResponseEntity<Object> getCanteen(@PathVariable int id) {
        Optional<FoodProvider> canteen = foodProviderRepository.getFoodProviderByIdAndType(id, canteenType());

        if(canteen.isEmpty()){
            return ResponseHandler.generateError(HttpStatus.NOT_FOUND, "No canteen with this id");
        }

        return ResponseHandler.generateResponseWithoutMessage(
                HttpStatus.OK,
                constructCanteenJsonObject(canteen.get())
        );
    }


    @GetMapping(value = "{id}/openinghours")
    public ResponseEntity<Object> getOpeningHours(@PathVariable int id) {
        List<OpeningHours> openingHours = openingHoursRepository.getOpeningHoursForCanteen(id);

        if(openingHours.isEmpty()){
            return ResponseHandler.generateError(HttpStatus.NOT_FOUND, "No opening hours found for this id");
        }

        return ResponseHandler.generateResponseWithoutMessage(HttpStatus.OK, constructOpeningHoursJsonObject(openingHours));
    }


    /**
     * Gets the menus for today and on
     * @param id
     * @return
     */
    @GetMapping(value = "{id}/menus/today-and-beyond")
    public ResponseEntity<Object> getMenusOfCanteenStartingFromToday(@PathVariable int id) {
        List<Menu> menus = menuRepository.getMealsForCanteenFromTodayOn(id);

        if(menus.isEmpty()){
            return ResponseHandler.generateError(HttpStatus.NOT_FOUND, "There are no menus for this id");
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


    @GetMapping(value = "{id}/menus/today")
    public ResponseEntity<Object> getTodaysMenu(@PathVariable int id) {
        List<Menu> menusForToday = menuRepository.findMenuByFoodProviderIdAndDate(id, LocalDate.now());

        if(menusForToday.isEmpty()){
            return ResponseHandler.generateError(HttpStatus.NOT_FOUND, "No menus for that canteen today");
        }

        return ResponseHandler.generateResponseWithoutMessage(HttpStatus.OK, constructMenuJsonObject(menusForToday, LocalDate.now()));
    }

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

    private String mealPriceToString(int price) {
        DecimalFormat d = new DecimalFormat("0.00", new DecimalFormatSymbols(Locale.GERMANY));
        return d.format(price / 100.0) + " \u20ac";
    }

    private JSONObject constructCanteenJsonObject(FoodProvider canteen) {
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

    private JSONArray constructOpeningHoursJsonObject(List<OpeningHours> openingHours) {
        JSONArray jA = new JSONArray();
        for (OpeningHours currentOpeningHours : openingHours) {
            JSONObject jO = new JSONObject();

            jO.put("weekday", currentOpeningHours.getWeekday().getName());
            jO.put("opensAt", currentOpeningHours.getOpensAt());
            jO.put("closesAt", currentOpeningHours.getClosesAt());
            jO.put("isOpened", currentOpeningHours.isOpened());

            jA.add(jO);
        }

        return jA;
    }

    private FoodProviderType canteenType(){
        return foodProviderTypeRepository.findByName(FetchedFoodProviderType.CANTEEN.getValue());
    }
}
