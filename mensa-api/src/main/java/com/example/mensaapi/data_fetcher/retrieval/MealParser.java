package com.example.mensaapi.data_fetcher.retrieval;

import com.example.mensaapi.data_fetcher.dataclasses.interfaces.FetchedMeal;
import com.example.mensaapi.data_fetcher.retrieval.interfaces.Parser;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Objects;
import java.util.Optional;

public class MealParser implements Parser<FetchedMeal> {


    /**
     * Returns parsed meal object from fetched element
     *
     * @param fetched expects element of class "menu"
     * @return returns {@link Optional< FetchedMeal >}
     */
    @Override
    public Optional<FetchedMeal> parse(Element fetched) {
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

        Elements ingredientElements = Objects.requireNonNull(fetched.getElementsByClass("additnr").first())
                .getElementsByTag("li");

        StringBuilder sb = new StringBuilder();

        for (Element ingredient:
                ingredientElements) {
            sb.append(ingredient.text()).append(";");
        }
        String ingredients = sb.substring(0, sb.length()-1);

        return Optional.of(FetchedMeal.createMeal(title, price, ingredients));
    }
}