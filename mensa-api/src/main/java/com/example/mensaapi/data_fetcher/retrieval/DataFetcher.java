package com.example.mensaapi.data_fetcher.retrieval;

import com.example.mensaapi.data_fetcher.dataclasses.FetchedData;
import com.example.mensaapi.data_fetcher.dataclasses.enums.Location;
import com.example.mensaapi.data_fetcher.dataclasses.interfaces.FetchedFoodProvider;
import com.example.mensaapi.data_fetcher.retrieval.interfaces.Fetcher;
import com.example.mensaapi.data_fetcher.retrieval.interfaces.Parser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DataFetcher implements Fetcher<FetchedData>{

    private FetchedData fetchedData;
    public DataFetcher() {
        fetchedData = new FetchedData();

        Optional<Document> docCanteens = Fetcher.createCanteensFetcher().fetchCurrentData();
        Parser<FetchedFoodProvider> foodProviderParser = Parser.createFoodProviderParser();

        docCanteens.ifPresent(document -> {
            Elements canteens = document.getElementsByClass("mensa");
            for (Element canteen: canteens) {
                fetchedData.addFetchedCanteen(foodProviderParser.parse(canteen).orElseThrow());
            }
        });

        for (Location loc:
             Location.values()) {
            Optional<Document> docCafeterias = Fetcher.createCafeteriasFetcher(loc).fetchCurrentData();

            docCafeterias.ifPresent(document -> {
                Elements cafeterias = document.getElementsByClass("mensa");
                for (Element cafeteria: cafeterias) {
                    fetchedData.addFetchedCafeteria(foodProviderParser.parse(cafeteria).orElseThrow());
                }
            });

        }

        //System.out.println(fetchedCanteens);
    }

    @Override
    public Optional<FetchedData> fetchCurrentData() {
        if (fetchedData == null || fetchedData.isEmpty()) return Optional.empty();
        return Optional.of(fetchedData);
    }
}
