package com.example.mensaapi.controller;

import com.example.mensaapi.database.entities.FoodProvider;
import com.example.mensaapi.database.entities.OpeningHours;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.List;

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
}
