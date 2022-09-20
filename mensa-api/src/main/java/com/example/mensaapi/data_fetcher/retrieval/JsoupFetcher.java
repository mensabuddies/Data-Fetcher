package com.example.mensaapi.data_fetcher.retrieval;

import com.example.mensaapi.data_fetcher.retrieval.interfaces.Fetcher;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URI;
import java.util.Optional;

public class JsoupFetcher implements Fetcher<Document> {
    private final String url;

    public JsoupFetcher(String url) {
        if (url == null)
            throw new IllegalArgumentException("URL cannot be null!");
        try {
            // Test if url is valid, ignore result
            URI.create(url);
        } catch (Exception e) {
            throw new IllegalArgumentException("'" + url + "'" + " is an invalid URL!");
        }
        this.url = url;
    }

    @Override
    public Optional<Document> fetchCurrentData() {
        try {
            return Optional.of(Jsoup.connect(url).get());
        } catch (IOException e) {
            return Optional.empty();
        }
    }
}
