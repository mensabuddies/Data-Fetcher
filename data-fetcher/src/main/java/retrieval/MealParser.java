package retrieval;

import dataclasses.interfaces.Meal;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import retrieval.interfaces.Parser;

import java.util.Optional;

public class MealParser implements Parser<Meal> {


    /**
     * Returns parsed meal object from fetched element
     *
     * @param fetched expects element of class "menu"
     * @return returns {@link Optional<Meal>}
     */
    @Override
    public Optional<Meal> parse(Elements fetched) {
        try {
            String name;
            int price;
            for (Element e:
                 fetched) {
                var temp = e.getElementsByClass("title");
                name = temp.get(0).text();
            }
           // name = fetched.get("class", "title").val();
            price = Integer.parseInt(
                    fetched.attr("class", "price")
                            .attr("data-default")
                            .replace(",", "")
            );

            return Optional.empty();

        } catch (Exception e) {
            return Optional.empty();
        }

    }
}
