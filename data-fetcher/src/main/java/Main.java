import dataclasses.Day;
import dataclasses.interfaces.Canteen;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import retrieval.interfaces.Fetcher;
import retrieval.interfaces.Parser;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
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
