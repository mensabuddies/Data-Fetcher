package com.example.mensaapi.database;

import com.example.mensaapi.data_fetcher.dataclasses.enums.FetchedFoodProviderType;
import com.example.mensaapi.data_fetcher.dataclasses.enums.MealComponentTypeEnum;
import com.example.mensaapi.database.entities.FoodProviderType;
import com.example.mensaapi.database.entities.Location;
import com.example.mensaapi.database.entities.MealComponentType;
import com.example.mensaapi.database.entities.Weekday;
import com.example.mensaapi.database.repositories.FoodProviderTypeRepository;
import com.example.mensaapi.database.repositories.LocationRepository;
import com.example.mensaapi.database.repositories.MealComponentTypeRepository;
import com.example.mensaapi.database.repositories.WeekdayRepository;

public class Util {


    public void insertLocations(LocationRepository repository) {
        String[] locations = {"Würzburg", "Schweinfurt", "Bamberg", "Aschaffenburg"};
        for (String location : locations) {
            repository.save(new Location(location));
        }
    }

    public void insertWeekdays(WeekdayRepository repository) {
        String[] weekdays = {"Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag", "Sonntag"};
        for (String day : weekdays) {
            repository.save(new Weekday(day));
        }
    }

    public void insertTypes(FoodProviderTypeRepository repository, MealComponentTypeRepository mealComponentTypeRepository) {
        // Has to be same order as enum!
        String[] foodProviderTypes = {FetchedFoodProviderType.CAFETERIA.getValue(), FetchedFoodProviderType.CANTEEN.getValue()};
        for (String type : foodProviderTypes) {
            repository.save(new FoodProviderType(type));
        }


        String[] mealComponentTypesConst = {MealComponentTypeEnum.ALLERGEN.getValue(), MealComponentTypeEnum.INGREDIENT.getValue()};
        for (String type: mealComponentTypesConst) {
            mealComponentTypeRepository.save(new MealComponentType(type));

        }
    }
}
