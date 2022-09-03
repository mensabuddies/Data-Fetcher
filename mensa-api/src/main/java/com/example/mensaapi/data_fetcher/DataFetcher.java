package com.example.mensaapi.data_fetcher;

import com.example.mensaapi.data_fetcher.dataclasses.interfaces.FetchedCanteen;
import com.example.mensaapi.data_fetcher.retrieval.interfaces.Fetcher;
import com.example.mensaapi.data_fetcher.retrieval.interfaces.Parser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DataFetcher {
    private List<FetchedCanteen> fetchedCanteens = new ArrayList<>();
    public DataFetcher() {
        Fetcher fetcher = Fetcher.createJSOUPFetcher("https://www.studentenwerk-wuerzburg.de/essen-trinken/speiseplaene.html");
        Optional<Document> doc = fetcher.fetchCurrentData();

        Parser<FetchedCanteen> canteenParser = Parser.createCanteenParser();

        doc.ifPresent(document -> {
            Elements mensen = document.getElementsByClass("mensa");
            for (Element mensa: mensen) {
                fetchedCanteens.add(canteenParser.parse(mensa).orElseThrow());
            }
        });
        System.out.println(fetchedCanteens);
    }

    public List<FetchedCanteen> get(){
        return fetchedCanteens;
    }
}
