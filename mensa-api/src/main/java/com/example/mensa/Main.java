package com.example.mensa;

import com.example.mensa.dataclasses.FetchedCanteen;
import com.example.mensa.dataclasses.FetchedData;
import com.example.mensa.dataclasses.interfaces.FetchedMeal;
import com.example.mensa.dataclasses.interfaces.FetchedOpeningHours;
import com.example.mensa.retrieval.DataFetcher;
import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firestore.v1.Document;
import com.google.firestore.v1.Write;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.WildcardType;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.format.TextStyle;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        FileInputStream serviceAccount = new FileInputStream("./AdminKey.json");

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        FirebaseApp.initializeApp(options);

        Firestore db = FirestoreClient.getFirestore();

        Optional<FetchedData> dataFetcher = new DataFetcher().fetchCurrentData();

        dataFetcher.ifPresent(data -> {
            var c = data.getFetchedCanteens();
            Integer id = 1;
            for(FetchedCanteen canteen : c){
                var canteenHashMap = createCanteenHashMap(id, canteen);

                WriteBatch batch = db.batch();

                DocumentReference canteenRef = db
                        .collection("canteens")
                        .document("location")
                        .collection(canteen.getLocation().getValue())
                        .document(canteen.getName());
                batch.set(canteenRef, canteenHashMap);

                CollectionReference canteenDescriptionReference = canteenRef
                        .collection("descriptions");

                batch.set(canteenDescriptionReference.document("de"), Map.of("value", canteen.getDescription()));
                batch.set(canteenDescriptionReference.document("en"), Map.of("value", canteen.getDescription()));

                // Menus --------------------------------------------------------------------------------------
                CollectionReference canteenMenuReference = canteenRef.collection("menus");

                for (var menu : canteen.getMenus()) {
                    for (var meal : menu.getMeals()) {
                        DocumentReference mealReference = canteenMenuReference
                                .document(menu.getDate().toString())
                                .collection("meals")
                                .document(meal.getName());

                        batch.set(mealReference, createMealHashMap(meal));

                        DocumentReference allergenReference = db.collection("additives").document("allergens");
                        DocumentReference ingredientReference = db.collection("additives").document("ingredients");
                        batch.set(allergenReference, Map.of("items", Arrays.stream(meal.getAllergensRaw().split(",")).toList()), SetOptions.merge());
                        batch.set(ingredientReference, Map.of("items", Arrays.stream(meal.getIngredientsRaw().split(",")).toList()), SetOptions.merge());

                    }
                }



                // --------------------------------------------------------------------------------------

                // Opening Hours
                CollectionReference openingHoursRef = canteenRef.collection("openingHours");

                for(FetchedOpeningHours o : canteen.getOpeningHours()){
                    batch.set(
                            openingHoursRef.document(o.getWeekday().getDisplayName(TextStyle.FULL, Locale.GERMAN)),
                            createOpeningHoursHashMap(o));
                }

                // asynchronously commit the batch ------------------------------------------------------
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
                id++;
            }
        });
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

    private static HashMap<String, Object> createOpeningHoursHashMap(FetchedOpeningHours openingHours){
        HashMap<String, Object> h = new HashMap<>();
        h.put("isOpen", openingHours.isOpen());
        h.put("opensAt", openingHours.getOpeningAt());
        h.put("closesAt", openingHours.getClosingAt());
        h.put("getFoodTill", openingHours.getGetAMealTill());

        return h;
    }

    private static HashMap<String, Object> createCanteenHashMap (Integer id, FetchedCanteen canteen){
        HashMap<String, Object> h = new HashMap<>();
        h.put("id", id);
        h.put("info", canteen.getTitleInfo());
        h.put("additionalInfo", canteen.getBodyInfo());
        h.put("address", canteen.getAddress());

        return h;
    }

    private static String mealPriceToString(int price) {
        DecimalFormat d = new DecimalFormat("0.00", new DecimalFormatSymbols(Locale.GERMANY));
        return d.format(price / 100.0) + " \u20ac";
    }
}
