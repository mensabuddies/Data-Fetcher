package retrieval.interfaces;

import org.jsoup.nodes.Document;
import retrieval.JsoupFetcher;

import java.util.Optional;

/**
 * AdapterClass for fetchers
 */
public interface Fetcher {
    Optional<Document> fetchCurrentData();

    static Fetcher createJSOUPFetcher(String url) {
        return new JsoupFetcher(url);
    }
}
