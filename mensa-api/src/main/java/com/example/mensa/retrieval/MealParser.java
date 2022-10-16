package com.example.mensa.retrieval;

import com.example.mensa.dataclasses.interfaces.FetchedMeal;
import com.example.mensa.retrieval.interfaces.Parser;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Objects;
import java.util.Optional;

public class MealParser implements Parser<FetchedMeal> {


    /**
     * Returns parsed meal object from fetched element
     *
     * @param fetched expects element of class "menu"
     * @return returns {@link Optional<FetchedMeal>}
     */
    @Override
    public Optional<FetchedMeal> parse(Element fetched) {
        String title;
        int priceStudent = -1;
        int priceEmployee = -1;
        int priceGuest = -1;

        if (!fetched.hasClass("menu")) {
            throw new IllegalStateException("Wrong element class for MealParser!");
        }

        Element titleElement = fetched.getElementsByClass("title").first();

        assert titleElement != null;

        title = titleElement.text();

        Element priceElement = fetched.getElementsByClass("price").first();

        try {
            assert priceElement != null;
            priceStudent = Integer.parseInt(priceElement.attr("data-default").replace(",", ""));
            priceEmployee = Integer.parseInt(priceElement.attr("data-bed").replace(",", ""));
            priceGuest = Integer.parseInt(priceElement.attr("data-guest").replace(",", ""));
        } catch (Exception e) {
            // ignore this case
            //throw new IllegalStateException("Could not parse html!");
        }

        Elements allergensElements = Objects
                .requireNonNull(fetched.getElementsByClass("additnr").first())
                .getElementsByTag("li");

        Elements ingredientsElements = Objects
                .requireNonNull(fetched.getElementsByClass("icon").first())
                .getElementsByClass("theicon");

        String allergens = getString(allergensElements, true).replace("ue", "ü");

        String ingredients = getString(ingredientsElements, false).replace("ue", "ü");

        return Optional.of(FetchedMeal.createMeal(title, priceStudent, priceEmployee, priceGuest, allergens, ingredients));
    }

    private String getString(Elements elements, boolean isAllergen) {
        if (elements.size() == 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder();

        for (Element e : elements) {
            if (isAllergen) {
                    sb.append(e.text()).append(",");
            } else {
                    sb.append(e.attr("title")).append(",");
            }
        }
        return sb.substring(0, sb.length() - 1);
    }
}
