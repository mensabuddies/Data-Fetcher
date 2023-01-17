package com.example.mensa;

import com.example.mensa.dataclasses.FetchedData;
import com.example.mensa.dataclasses.PubSubMessage;
import com.example.mensa.dataclasses.enums.FetchedFoodProviderType;
import com.example.mensa.dataclasses.interfaces.FetchedFoodProvider;
import com.example.mensa.dataclasses.interfaces.FetchedMeal;
import com.example.mensa.dataclasses.interfaces.FetchedOpeningHours;
import com.example.mensa.retrieval.DataFetcher;
import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.cloud.functions.BackgroundFunction;
import com.google.cloud.functions.Context;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Main implements BackgroundFunction<PubSubMessage> {

    private static final boolean DEBUG = true;

    private static final int ALLERGENS = 1;
    private static final int INGREDIENTS = 2;

    private static WriteBatch batch = null;

    private static Firestore db = null;

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    @Override
    public void accept(PubSubMessage message, Context context) throws Exception {
        String data = message.data != null
                ? new String(Base64.getDecoder().decode(message.data))
                : "Hello, World";
        logger.info(data);
        mainFunction();
    }

    // For local testing

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        mainFunction();
    }


    public static void mainFunction() throws IOException, ExecutionException, InterruptedException {
        FirebaseOptions options;
        if (DEBUG) {
            FileInputStream serviceAccount = new FileInputStream("./AdminKey.json");

            options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
        } else {
            options = FirebaseOptions.builder()
                    .setProjectId("mensa-api-v2")
                    .setCredentials(GoogleCredentials.getApplicationDefault())
                    .setDatabaseUrl("https://mensa-api-v2.firebaseio.com/")
                    .build();
        }

        if(FirebaseApp.getApps().isEmpty()){
            FirebaseApp.initializeApp(options);
        } else {
            FirebaseApp.getApps().get(0);
        }

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
/*
        CollectionReference foodProviderDescriptionReference = foodProviderRef
                .collection("descriptions");

        setAndCommitIfNeeded(foodProviderDescriptionReference.document(Locale.GERMAN.getLanguage()), Map.of(
                "description", foodProvider.getDescription(),
                "language", Locale.GERMAN.getLanguage()
        ));
        setAndCommitIfNeeded(foodProviderDescriptionReference.document(Locale.ENGLISH.getLanguage()), Map.of(
                "value", "not yet implemented",
                "language", Locale.ENGLISH.getLanguage()
        ));
*/
        // Menus --------------------------------------------------------------------------------------
        if (foodProvider.getType() == FetchedFoodProviderType.CANTEEN) {
            CollectionReference canteenMenuReference = foodProviderRef.collection("menus");

            for (var menu : foodProvider.getMenus()) {
                for (var meal : menu.getMeals()) {
                    //DocumentReference menuReference = canteenMenuReference
                    //        .document(menu.getDate().toString());

                    // Save date in meal, not menu
                    // setAndCommitIfNeeded(menuReference, Map.of("date", menu.getDate().toString()));

                    DocumentReference mealReference = canteenMenuReference
                            .document(meal.getName() + "_" + menu.getDate()); // Each meal gets a unique ID consisting of the name and date

                    setAndCommitIfNeeded(mealReference, createMealHashMap(meal, menu.getDate(), id));


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

    }

    private static Map<String, Object> createMealHashMap(FetchedMeal meal, LocalDate date, Integer foodProviderId) {
        return Map.of(
                "name", meal.getName(),
                "foodProviderId", foodProviderId,
                "date", date.toString(),
                "priceGuest", mealPriceToString(meal.getPriceGuest()),
                "priceEmployee", mealPriceToString(meal.getPriceEmployee()),
                "priceStudent", mealPriceToString(meal.getPriceStudent()),
                "allergens", meal.getAllergensRaw(),
                "ingredients", meal.getIngredientsRaw()
        );
    }

    private static List<Object> createOpeningHoursList(DayOfWeek dayOfWeek, List<FetchedOpeningHours> openingHours) {
        List<Object> array = new ArrayList<>();
        // For data consistency the opening hours of one day should be sorted according to openingAt
        var sorted = openingHours.stream().filter(o -> o.getWeekday() == dayOfWeek).sorted(new Comparator<FetchedOpeningHours>() {
            @Override
            public int compare(FetchedOpeningHours o1, FetchedOpeningHours o2) {
                var value1 = Integer.valueOf(o1.getOpeningAt().replace(".", ""));
                var value2 = Integer.valueOf(o2.getOpeningAt().replace(".", ""));
                return value1.compareTo(value2);
            }
        }).toList();


        for (var openingHour : sorted) {
            array.add(openingHour.getOpeningAt());
            array.add(openingHour.getClosingAt());
            array.add(openingHour.getGetAMealTill());
            array.add(openingHour.isOpen());
        }
        return array;
    }

    private static HashMap<String, ?> createFoodProviderHashMap(Integer id, FetchedFoodProvider foodProvider) {
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
        h.put("description_de", foodProvider.getDescription());
        h.put("location", foodProvider.getLocation().getValue());

        var sorted = foodProvider
                .getOpeningHours().stream()
                .sorted(
                        Comparator.comparingInt(o -> o.getWeekday().ordinal())
                ).toList();

        for (var openingHour : sorted) {
            h.put("hours_" +
                            openingHour
                                    .getWeekday()
                                    .getDisplayName(TextStyle.SHORT_STANDALONE, Locale.ENGLISH)
                                    .toLowerCase(),
                    createOpeningHoursList(
                            openingHour.getWeekday(),
                            foodProvider.getOpeningHours()
                    )
            );
        }

        return h;
    }

    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
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

    private static void setAndCommitIfNeeded(DocumentReference df, Map<String, ?> value) {
        commitIfNeeded();
        batch.set(df, value);
    }


    private static String mealPriceToString(int price) {
        DecimalFormat d = new DecimalFormat("0.00", new DecimalFormatSymbols(Locale.GERMANY));
        return d.format(price / 100.0) + "\u20ac";
    }

    private static Date dateFromLocalDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}
