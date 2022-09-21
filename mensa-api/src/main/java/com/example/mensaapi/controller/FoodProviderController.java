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

    @GetMapping(value = "/canteens")
    public ResponseEntity<Object> getCanteens() {
        List<FoodProvider> canteens =
                foodProviderRepository.getFoodProvidersByType(
                        foodProviderTypeRepository.findByName(FetchedFoodProviderType.CANTEEN.getValue())
                ).orElseThrow();
        JSONArray canteensArray = new JSONArray();

        for (FoodProvider currentCanteen : canteens) {
            canteensArray.add(constructCanteenJsonObject(currentCanteen));
        }
        return ResponseHandler.generateResponse(HttpStatus.OK, canteensArray);
    }

    @GetMapping(value = "/canteens/{id}")
    public ResponseEntity<Object> getCanteen(@PathVariable int id) {
        FoodProvider c = foodProviderRepository.findById(id).orElse(null);

        if (c != null && c.getType().getName().equals(FetchedFoodProviderType.CANTEEN.getValue())) {
            return ResponseHandler.generateResponse(HttpStatus.OK, constructCanteenJsonObject(c));
        }
        return ResponseHandler.generateResponse("Canteen not found", HttpStatus.NOT_FOUND, null);

    }

    @GetMapping(value = "/canteens/{id}/openinghours")
    public ResponseEntity<Object> getOpeningHours(@PathVariable int id) {
        AtomicReference<ResponseEntity<Object>> re = new AtomicReference<>();
        openingHoursRepository.findByFoodProviderIdEqualsOrderByWeekday(id).ifPresentOrElse(openingHours -> {
                    re.set(ResponseHandler.generateResponse(
                            HttpStatus.OK,
                            constructOpeningHoursJsonObject(openingHours))
                    );
                }, () -> {
                    re.set(ResponseHandler.generateResponse(
                            "Canteen not found",
                            HttpStatus.NOT_FOUND,
                            null)
                    );
                }
        );
        return re.get();
    }

    @GetMapping(value = "/canteens/{id}/menus")
    public ResponseEntity<Object> getMenusOfCanteen(@PathVariable int id) {
        AtomicReference<ResponseEntity<Object>> re = new AtomicReference<>();

        menuRepository.findByFoodProviderIdEqualsOrderByDate(id).ifPresentOrElse(menus -> {
            Set<LocalDate> dates = new HashSet<>();
            for (Menu menu : menus) {
                dates.add(menu.getDate());
            }

            JSONArray jA = new JSONArray();
            for (LocalDate date : dates) {
                List<Menu> menusForToday = menuRepository.findByFoodProviderIdEqualsAndDate(id, date).orElse(new ArrayList<>());
                jA.add(constructMenuJsonObject(menusForToday, date));
            }
            Collections.reverse(jA);

            re.set(ResponseHandler.generateResponse(HttpStatus.OK, jA));
        }, () -> {
            re.set(ResponseHandler.generateResponse(
                    "Canteen not found",
                    HttpStatus.NOT_FOUND,
                    null)
            );
        });

        return re.get();
    }

    @GetMapping(value = "/canteens/{id}/menus/today_and_beyond")
    public ResponseEntity<Object> getMenusOfCanteenStartingFromToday(@PathVariable int id) {
        AtomicReference<ResponseEntity<Object>> re = new AtomicReference<>();

        menuRepository.findByFoodProviderIdEqualsOrderByDate(id).ifPresentOrElse(menus -> {
            Set<LocalDate> dates = new HashSet<>();
            menus.stream()
                    .filter(menu -> menu.getDate().compareTo(LocalDate.now()) >= 0)
                    .map(menu -> dates.add(menu.getDate()))
                    .collect(Collectors.toList());

            JSONArray jA = new JSONArray();
            for (LocalDate date : dates) {
                List<Menu> menusForToday = menuRepository.findByFoodProviderIdEqualsAndDate(id, date).orElse(new ArrayList<>());
                jA.add(constructMenuJsonObject(menusForToday, date));
            }
            Collections.reverse(jA);

            re.set(ResponseHandler.generateResponse(HttpStatus.OK, jA));
        }, () -> {
            re.set(ResponseHandler.generateResponse(
                    "Canteen not found",
                    HttpStatus.NOT_FOUND,
                    null)
            );
        });

        return re.get();
    }

    @GetMapping(value = "/canteens/{id}/menus/today")
    public ResponseEntity<Object> getTodaysMenu(@PathVariable int id) {
        AtomicReference<ResponseEntity<Object>> re = new AtomicReference<>();
        menuRepository.findByFoodProviderIdEqualsAndDate(id, LocalDate.now()).ifPresentOrElse(
                menus -> re.set(
                        ResponseHandler.generateResponse(
                                HttpStatus.OK,
                                constructMenuJsonObject(menus, LocalDate.now())
                        )
                ), () -> {
                    re.set(ResponseHandler.generateResponse(
                            "Canteen not found",
                            HttpStatus.NOT_FOUND,
                            null)
                    );
                }
        );

        return re.get();
    }

    private JSONObject constructMenuJsonObject(List<Menu> menus, LocalDate date) {
        JSONArray jA = new JSONArray();
        for (Menu menu : menus) {
            jA.add(constructMealJsonObject(menu.getMeal()));
        }

        JSONObject jO = new JSONObject();
        jO.put(date, jA);
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
        jO.put("foodProviderType", canteen.getType().getName());


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
}