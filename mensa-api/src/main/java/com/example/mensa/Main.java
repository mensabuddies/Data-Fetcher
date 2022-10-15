package com.example.mensa;

import com.example.mensa.dataclasses.FetchedData;
import com.example.mensa.retrieval.DataFetcher;
import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        /*Optional<FetchedData> dataFetcher = new DataFetcher().fetchCurrentData();

        dataFetcher.ifPresent(document -> {

        });*/
        FileInputStream serviceAccount = new FileInputStream("./AdminKey.json");

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("http://localhost:8080")
                .build();

        FirebaseApp.initializeApp(options);

        Firestore db = FirestoreClient.getFirestore();

        Main getHelloWorld = new Main();
        HashMap<String, String> helloWorld = getHelloWorld.getHelloWorldFromLocal();
        ApiFuture<WriteResult> future = db.collection("test").document("testMessages")
                .set(helloWorld);

        System.out.println("Successfully updated at: " + future.get().getUpdateTime());
    }

    private HashMap<String, String> getHelloWorldFromLocal() {
        HashMap<String, String> res = new HashMap<>();
        res.put("message", "Hello World");
        return res;
    }
}
