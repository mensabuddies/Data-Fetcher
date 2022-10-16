package com.example.mensa;

import com.example.mensa.dataclasses.FetchedCanteen;
import com.example.mensa.dataclasses.FetchedData;
import com.example.mensa.dataclasses.enums.FetchedFoodProviderType;
import com.example.mensa.dataclasses.interfaces.FetchedFoodProvider;
import com.example.mensa.dataclasses.interfaces.FetchedMeal;
import com.example.mensa.dataclasses.interfaces.FetchedOpeningHours;
import com.example.mensa.retrieval.DataFetcher;
import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.format.TextStyle;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class Main {

    private static final int ALLERGENS = 1;
    private static final int INGREDIENTS = 2;

    private static WriteBatch batch = null;

    private static Firestore db = null;

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        FileInputStream serviceAccount = new FileInputStream("./AdminKey.json");

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        FirebaseApp.initializeApp(options);

        db = FirestoreClient.getFirestore();

        Optional<FetchedData> dataFetcher = new DataFetcher().fetchCurrentData();

        var additives = new HashMap<Integer, Set<String>>();
        CollectionReference additivesReference = db.collection("additives");

        batch = db.batch();

        dataFetcher.ifPresent(data -> {
            var f = data.getFetchedFoodProviders();
            Integer id = 1;
            for (FetchedFoodProvider foodProvider : f) {
                createFoodProviderEntry(
                        id,
                        batch,
                        foodProvider,
                        db,
                        additives
                );
                id++;
            }

            /*
                Some formatting has to be done. For example "Rind/Kalb" is interpreted as a path
                so replace '/' with something else
            */

            for (var allergen : additives.get(ALLERGENS)) {
                if (!allergen.isBlank())
                    setAndCommitIfNeeded(additivesReference.document(allergen.replace("/", "-")), Map.of(
                            "type", "allergen",
                            "name", allergen
                    ));
            }
            for (var ingredient : additives.get(INGREDIENTS)) {
                if (!ingredient.isBlank())
                    setAndCommitIfNeeded(additivesReference.document(ingredient.replace("/", "-")), Map.of(
                            "type", "ingredient",
                            "name", ingredient
                    ));
            }

            ApiFuture<List<WriteResult>> future = batch.commit();

            try {
                for (WriteResult result : future.get()) {
                    System.out.println("Update time : " + result.getUpdateTime());
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private static void createFoodProviderEntry(int id, WriteBatch batch, FetchedFoodProvider foodProvider, Firestore db, HashMap<Integer, Set<String>> additives) {
        var type = foodProvider.getType().getValue();

        DocumentReference foodProviderRef = db
                .collection("foodProviders")
                .document(foodProvider.getName());

        var foodProviderHashMap = createFoodProviderHashMap(id, foodProvider);


        setAndCommitIfNeeded(foodProviderRef, foodProviderHashMap);

        CollectionReference foodProviderDescriptionReference = foodProviderRef
                .collection("descriptions");

        setAndCommitIfNeeded(foodProviderDescriptionReference.document("de"), Map.of("value", foodProvider.getDescription()));
        setAndCommitIfNeeded(foodProviderDescriptionReference.document("en"), Map.of("value", foodProvider.getDescription()));

        // Menus --------------------------------------------------------------------------------------
        if (foodProvider.getType() == FetchedFoodProviderType.CANTEEN) {
            CollectionReference canteenMenuReference = foodProviderRef.collection("menus");

            for (var menu : foodProvider.getMenus()) {
                for (var meal : menu.getMeals()) {
                    DocumentReference menuReference = canteenMenuReference
                            .document(menu.getDate().toString());

                    setAndCommitIfNeeded(menuReference, Map.of("date", menu.getDate().toString()));

                    DocumentReference mealReference = menuReference
                            .collection("meals")
                            .document(meal.getName());

                    setAndCommitIfNeeded(mealReference, createMealHashMap(meal));


                    if (!additives.containsKey(ALLERGENS)) {
                        var hashSet = new HashSet<>(List.of(meal.getAllergensRaw().split(",")));
                        additives.put(ALLERGENS, hashSet);
                    } else {
                        var temp = additives.get(ALLERGENS);
                        temp.addAll(
                                Set.of(meal.getAllergensRaw().split(","))
                        );
                        additives.put(ALLERGENS, temp);
                    }

                    if (!additives.containsKey(INGREDIENTS)) {
                        var hashSet = new HashSet<>(List.of(meal.getIngredientsRaw().split(",")));
                        additives.put(INGREDIENTS, hashSet);
                    } else {
                        var temp = additives.get(INGREDIENTS);
                        temp.addAll(
                                Set.of(meal.getIngredientsRaw().split(","))
                        );
                        additives.put(INGREDIENTS, temp);
                    }
                }
            }
        }
        // --------------------------------------------------------------------------------------

        // Opening Hours
        CollectionReference openingHoursRef = foodProviderRef.collection("openingHours");

        for (FetchedOpeningHours o : foodProvider.getOpeningHours()) {
            setAndCommitIfNeeded(
                    openingHoursRef.document(o.getWeekday().getDisplayName(TextStyle.FULL, Locale.GERMAN)), // TODO: Maybe change this to just the number?
                    createOpeningHoursHashMap(o));
        }
    }

    private static Map<String, Object> createMealHashMap(FetchedMeal meal) {
        return Map.of(
                "priceGuest", mealPriceToString(meal.getPriceGuest()),
                "priceEmployee", mealPriceToString(meal.getPriceEmployee()),
                "priceStudent", mealPriceToString(meal.getPriceStudent()),
                "allergens", meal.getAllergensRaw(),
                "ingredients", meal.getIngredientsRaw()
        );
    }

    private static HashMap<String, Object> createOpeningHoursHashMap(FetchedOpeningHours openingHours) {
        HashMap<String, Object> h = new HashMap<>();
        h.put("isOpen", openingHours.isOpen());
        h.put("opensAt", openingHours.getOpeningAt());
        h.put("closesAt", openingHours.getClosingAt());
        h.put("getFoodTill", openingHours.getGetAMealTill());

        return h;
    }

    private static HashMap<String, Object> createFoodProviderHashMap(Integer id, FetchedFoodProvider foodProvider) {
        HashMap<String, Object> h = new HashMap<>();

        // e.g. "Mensateria Campus Hubland Nord Würzburg"
        var longName = foodProvider.getName();

        var firstWhiteSpace = longName.indexOf(" ");
        var lastWhiteSpace = longName.lastIndexOf(" ");

        var type = longName.substring(0, firstWhiteSpace);
        var name = capitalize(longName.substring(firstWhiteSpace + 1, lastWhiteSpace));

        h.put("id", id);
        h.put("info", foodProvider.getTitleInfo());
        h.put("additionalInfo", foodProvider.getBodyInfo());
        h.put("address", foodProvider.getAddress());
        // Whether it is a canteen or cafeteria
        h.put("category", foodProvider.getType().getValue());
        // Whether it is a mensateria, interimsmensa etc.
        h.put("type", type);
        h.put("name", name);
        h.put("location", foodProvider.getLocation().getValue());

        return h;
    }

    private static String capitalize(String str) {
        if(str == null || str.isEmpty()) {
            return str;
        }

        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private static void commitIfNeeded() {
        if (batch.getMutationsSize() >= 500) {
            ApiFuture<List<WriteResult>> future = batch.commit();

            try {
                for (WriteResult result : future.get()) {
                    System.out.println("Update time : " + result.getUpdateTime());
                }
                batch = db.batch();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void setAndCommitIfNeeded(DocumentReference df, Map<String, Object> value) {
        commitIfNeeded();
        batch.set(df, value);
    }


    private static String mealPriceToString(int price) {
        DecimalFormat d = new DecimalFormat("0.00", new DecimalFormatSymbols(Locale.GERMANY));
        return d.format(price / 100.0) + " \u20ac";
    }
}
