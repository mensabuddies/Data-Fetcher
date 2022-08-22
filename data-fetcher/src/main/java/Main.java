import dataclasses.interfaces.Meal;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import retrieval.interfaces.Fetcher;
import retrieval.interfaces.Parser;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws IOException {
        //Canteens c = new Canteens();

        Fetcher fetcher = Fetcher.createJSOUPFetcher("https://www.studentenwerk-wuerzburg.de/essen-trinken/speiseplaene/mensateria-campus-hubland-nord-wuerzburg.html");
        Parser<Meal> mealParser = Parser.createMealParser();

        Optional<Document> doc = fetcher.fetchCurrentData();

        Set<Meal> meals = new HashSet<>();

        doc.ifPresent(document -> {
            Elements extracted = document.getElementsByClass("menu");

            meals.add(mealParser.parse(extracted).get());


        });
        System.out.println(doc.isPresent());
    }
}
