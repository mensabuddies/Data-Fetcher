package retrieval.interfaces;

import org.jsoup.select.Elements;
import retrieval.MealParser;

import java.util.Optional;

public interface Parser<T>{
    Optional<T> parse(Elements fetched);

    static MealParser createMealParser() {
        return new MealParser();
    }
}
