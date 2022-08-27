package com.example.mensaapi.data_fetcher;

import com.example.mensaapi.data_fetcher.dataclasses.interfaces.Canteen;
import com.example.mensaapi.data_fetcher.retrieval.interfaces.Fetcher;
import com.example.mensaapi.data_fetcher.retrieval.interfaces.Parser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DataFetcher {
    public void fetchData() {
        //Canteens c = new Canteens();

        Fetcher fetcher = Fetcher.createJSOUPFetcher("https://www.studentenwerk-wuerzburg.de/wuerzburg/essen-trinken/speiseplaene.html");
        Optional<Document> doc = fetcher.fetchCurrentData();

        List<Canteen> canteens = new ArrayList<>();
        Parser<Canteen> canteenParser = Parser.createCanteenParser();

        doc.ifPresent(document -> {
            Elements mensen = document.getElementsByClass("mensa");
            for (Element mensa: mensen) {
                canteens.add(canteenParser.parse(mensa).orElseThrow());
            }
        });
        System.out.println(canteens);
    }
}
