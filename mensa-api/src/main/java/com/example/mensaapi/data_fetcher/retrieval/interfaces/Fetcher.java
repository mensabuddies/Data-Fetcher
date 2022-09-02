package com.example.mensaapi.data_fetcher.retrieval.interfaces;

import com.example.mensaapi.data_fetcher.retrieval.JsoupFetcher;
import org.jsoup.nodes.Document;

import java.util.Optional;

/**
 * AdapterClass for fetchers
 */
public interface Fetcher {
    Optional<Document> fetchCurrentData();

    static Fetcher createJSOUPFetcher(String url) {
        return new JsoupFetcher(url);
    }

}
