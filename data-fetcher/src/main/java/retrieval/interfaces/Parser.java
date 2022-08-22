package retrieval.interfaces;

import org.jsoup.nodes.Element;
import retrieval.DayParser;
import retrieval.MealParser;

import java.util.Optional;

public interface Parser<T>{
    Optional<T> parse(Element fetched);

    static MealParser createMealParser() {
        return new MealParser();
    }
    static DayParser createDayParser() { return new DayParser(createMealParser()); }
}
