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

    private static final int ALLERGENS = 1;
    private static final int INGREDIENTS = 2;

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        FileInputStream serviceAccount = new FileInputStream("./AdminKey.json");

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        FirebaseApp.initializeApp(options);

        Firestore db = FirestoreClient.getFirestore();

        Optional<FetchedData> dataFetcher = new DataFetcher().fetchCurrentData();

        var additives = new HashMap<Integer, Set<String>>();
        CollectionReference additivesReference = db.collection("additives");

        WriteBatch batch = db.batch();

        dataFetcher.ifPresent(data -> {
            var c = data.getFetchedCanteens();
            Integer id = 1;
            for (FetchedCanteen canteen : c) {
                var canteenHashMap = createCanteenHashMap(id, canteen);


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

                        /*
                            Some formatting has to be done. For example "Rind/Kalb" is interpreted as a path
                            so replace '/' with something else

                            TODO: Some additives are both allergen and ingredient. As there are only a few ingredients
                            TODO: they should be classified as ingredient (they are usually ingredients in first place)
                         */

                        if (!additives.containsKey(ALLERGENS)) {
                            var hashSet = new HashSet<>(List.of(meal.getAllergensRaw().replace("/", "-").split(",")));
                            additives.put(ALLERGENS, hashSet);
                        } else {
                            var temp = additives.get(ALLERGENS);
                            temp.addAll(
                                    Set.of(meal.getAllergensRaw().replace("/", "-").split(","))
                            );
                            additives.put(ALLERGENS, temp);
                        }

                        if (!additives.containsKey(INGREDIENTS)) {
                            var hashSet = new HashSet<>(List.of(meal.getIngredientsRaw().replace("/", "-").split(",")));
                            additives.put(INGREDIENTS, hashSet);
                        } else {
                            var temp = additives.get(INGREDIENTS);
                            temp.addAll(
                                    Set.of(meal.getIngredientsRaw().replace("/", "-").split(","))
                            );
                            additives.put(INGREDIENTS, temp);
                        }
                    }
                }
                // --------------------------------------------------------------------------------------

                // Opening Hours
                CollectionReference openingHoursRef = canteenRef.collection("openingHours");

                for (FetchedOpeningHours o : canteen.getOpeningHours()) {
                    batch.set(
                            openingHoursRef.document(o.getWeekday().getDisplayName(TextStyle.FULL, Locale.GERMAN)),
                            createOpeningHoursHashMap(o));
                }
                id++;
            }

            for (var allergen : additives.get(ALLERGENS)) {
                if (!allergen.isBlank())
                    batch.set(additivesReference.document(allergen), Map.of("type", "allergen"));
            }
            for (var ingredient : additives.get(INGREDIENTS)) {
                if (!ingredient.isBlank())
                    batch.set(additivesReference.document(ingredient), Map.of("type", "ingredient"));
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

    private static HashMap<String, Object> createCanteenHashMap(Integer id, FetchedCanteen canteen) {
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
