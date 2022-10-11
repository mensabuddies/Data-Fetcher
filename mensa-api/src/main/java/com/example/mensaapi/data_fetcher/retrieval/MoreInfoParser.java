package com.example.mensaapi.data_fetcher.retrieval;

import com.example.mensaapi.data_fetcher.dataclasses.enums.Information;
import com.example.mensaapi.data_fetcher.retrieval.interfaces.Parser;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.NodeFilter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class MoreInfoParser implements Parser<Map<Information, String>> {

    /**
     * Expects div element of class "content"
     * @param fetched
     * @return
     */
    @Override
    public Optional<Map<Information, String>> parse(Element fetched) {
        Map<Information, String> result = new HashMap<>();

        try {
            var raw = "";
            var element = Objects.requireNonNull(fetched.getElementsByTag("strong").first()).childNode(0);
            while (!(element instanceof TextNode) && element.childNodes().size() != 0)
                element = element.childNode(0);

            result.put(Information.ADDRESS,
                    ((TextNode) element).getWholeText()
                            .replace("&shy;", "")
                    // more here if needed
            );
        } catch (Exception e) {
            // TODO: Bad practice
            result.put(Information.ADDRESS, "");
        }

        try {
            result.put(Information.DESCRIPTION,
                    fetched.getElementsByTag("p").stream()
                            .flatMap(element -> element.childNodes().stream())
                            .filter(element ->
                                    element instanceof TextNode
                            )
                            .map(element -> ((TextNode) element).getWholeText())
                            .collect(Collectors.joining("\n\n"))
            );
        } catch (Exception e) {
            result.put(Information.DESCRIPTION, "");
        }
        return Optional.of(result);
    }
}
