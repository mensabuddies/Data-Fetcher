package com.example.mensa.retrieval.interfaces;

import com.example.mensa.dataclasses.enums.Location;
import com.example.mensa.retrieval.JsoupFetcher;
import org.jsoup.nodes.Document;

import java.util.Optional;

/**
 * AdapterClass for fetchers
 */
public interface Fetcher<T> {
    Optional<T> fetchCurrentData();

    static Fetcher<Document> createCanteensFetcher() {
        return new JsoupFetcher("https://www.studentenwerk-wuerzburg.de/essen-trinken/speiseplaene.html");
    }

    static Fetcher<Document> createJsoupFetcher(String url) {
        return new JsoupFetcher(url);
    }

    static Fetcher<Document> createCafeteriasFetcher(Location location) {
        return new JsoupFetcher("https://www.studentenwerk-wuerzburg.de/" + location.getValueFormatted() + "/essen-trinken/cafeterien.html");
    }

}
