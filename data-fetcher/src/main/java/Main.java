import dataclasses.Day;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import retrieval.interfaces.Fetcher;
import retrieval.interfaces.Parser;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        //Canteens c = new Canteens();

        Fetcher fetcher = Fetcher.createJSOUPFetcher("https://www.studentenwerk-wuerzburg.de/essen-trinken/speiseplaene/mensateria-campus-hubland-nord-wuerzburg.html");
        Parser<Day> dayParser = Parser.createDayParser();

        Optional<Document> doc = fetcher.fetchCurrentData();
        List<Day> dayList = new ArrayList<>();
        doc.ifPresent(document -> {
            Elements days = document.getElementsByClass("day").stream().filter(dayElement -> dayElement.tagName().equals("div")).collect(Collectors.toCollection(Elements::new));
            for (Element day: days) {
                dayList.add(dayParser.parse(day).orElseThrow());
            }
        });
        System.out.println(dayList);
    }
}
