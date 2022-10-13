package com.example.mensaapi.controller;

import com.example.mensaapi.database.entities.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.lang.Nullable;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class JsonCreators {
    public static JSONObject constructFoodProviderJsonObject(FoodProvider fp) {
        JSONObject jO = new JSONObject();
        jO.put("id", fp.getId());
        jO.put("name", fp.getName());
        jO.put("location", fp.getLocation().getName());
        jO.put("openingHours", constructOpeningHoursJsonObject(fp.getOpeningHours()));
        jO.put("info", fp.getInfo());
        jO.put("additionalInfo", fp.getAdditionalInfo());
        jO.put("linkToFoodPlan", fp.getLinkToFoodPlan());
        jO.put("type", fp.getType().getName());
        jO.put("description", fp.getDescription());
        jO.put("address", fp.getAddress());

        return jO;
    }

    public static JSONObject constructMealComponentJsonObject(MealComponent mc) {
        JSONObject jO = new JSONObject();
        jO.put("id", mc.getId());
        jO.put("name", mc.getName());
        return jO;
    }

    public static JSONObject constructMealComponentJsonObject(MealComponent mc, MealComponentType type) {
        JSONObject jO = constructMealComponentJsonObject(mc);
        jO.put("type", type.getName());
        return jO;
    }

    public static JSONArray constructOpeningHoursJsonObject(List<OpeningHours> openingHours) {
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

    /**
     * Helper function to create a JSON object for a menu.
     * @param menus
     * @param date
     * @return
     */
    public static JSONObject constructMenuJsonObject(List<Menu> menus, LocalDate date) {
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
    public static JSONObject constructMealJsonObject(Meal meal) {
        JSONObject jO = new JSONObject();
        jO.put("id", meal.getId());
        jO.put("name", meal.getName());
        jO.put("priceStudent", mealPriceToString(meal.getPriceStudent()));
        jO.put("priceEmployee", mealPriceToString(meal.getPriceEmployee()));
        jO.put("priceGuest", mealPriceToString(meal.getPriceGuest()));
        jO.put("allergens", meal.getAllergens().stream().map(MealComponent::getName).collect(Collectors.joining(",")));
        jO.put("ingredients", meal.getIngredients().stream().map(MealComponent::getName).collect(Collectors.joining(",")));;

        return jO;
    }

    /**
     * Helper function to convert the prices stored as an int to Euro.
     * @param price
     * @return
     */
    private static String mealPriceToString(int price) {
        DecimalFormat d = new DecimalFormat("0.00", new DecimalFormatSymbols(Locale.GERMANY));
        return d.format(price / 100.0) + " \u20ac";
    }
}
