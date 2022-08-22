package retrieval;

import dataclasses.interfaces.Meal;
import org.jsoup.nodes.Element;
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
    public Optional<Meal> parse(Element fetched) {
        String title = "";
        int price = -1;

        if (!fetched.hasClass("menu")){
            throw new IllegalStateException("Wrong element class for MealParser!");
        }

        Element titleElement = fetched.getElementsByClass("title").first();

        assert titleElement != null;

        title = titleElement.text();

        Element priceElement = fetched.getElementsByClass("price").first();

        try {
            assert priceElement != null;
            price = Integer.parseInt(priceElement.attr("data-default").replace(",", ""));
        } catch (Exception e){
            throw new IllegalStateException("Could not parse html!");
        }

        return Optional.of(Meal.createMeal(title, price));
    }
}
