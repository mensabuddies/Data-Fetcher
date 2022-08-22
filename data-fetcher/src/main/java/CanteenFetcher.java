import dataclasses.Canteen;
import dataclasses.Location;
import dataclasses.OpeningHours;
import dataclasses.Weekday;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CanteenFetcher {
    public CanteenFetcher() throws IOException {
        fetchCanteens();
    }

    // TODO: Check if this also works when the evening mensa is back again

    /**
     * Format of the site:
     * <pre>{@code
     *     <div class="right">
     *         <h4>Mensa am Studentenhaus Würzburg</h4>
     *         <div class="opening">
     *             <table class="contenttable responsive">
     *                 <tbody>
     *                     <tr>
     *                         <th scope="row">Mo - Fr</th>
     *                         <td>11.00 - 14.30 Uhr</td>
     *                         <td>&nbsp;&nbsp; Essensausgabe bis 14.00 Uhr</td>
     *                     </tr>
     *                 </tbody>
     *              </table>
     *              <p><strong>Abend­mensa in der Burse am Stu­den­ten­haus: Mo - Do 14.00 - 18.00 Uhr, Es­sen­saus­gabe bis 17.30 Uhr</strong></p>
     *          </div>
     *          <a class="fi" href="/essen-trinken/speiseplaene/mensa-am-studentenhaus-wuerzburg.html">
     * 				zum Speiseplan
     * 			</a>
     *      </div>
     * </pre>}
     *
     * @throws IOException
     */
    private void fetchCanteens() throws IOException {
        Document document = Jsoup.connect("https://www.studentenwerk-wuerzburg.de/essen-trinken/speiseplaene.html").get();
        Elements canteens = document.getElementsByClass("mensa");
        for (Element canteen : canteens) {
            Canteen c = new Canteen();
            Elements details = canteen.getElementsByClass("right");

            for (Element detail : details) {
                // Get Name
                String nameElement = detail.getElementsByTag("h4").text();
                // Info text (e.g. mensa is closed) is directly after the mensa-name in a h4-Tag, seperated by hyphen or pipe
                if (nameElement.contains(" - ")) {
                    String[] parts = nameElement.split(" - ");
                    c.setName(parts[0]);
                    c.setInfo(parts[1]);
                } else if (nameElement.contains(" | ")) {
                    String[] parts = nameElement.split(" \\| ");
                    c.setName(parts[0]);
                    c.setInfo(parts[1]);
                } else {
                    // If none of those delimiters can be found, we assume there is no info text.
                    c.setName(nameElement);
                }

                // Get the city the mensa is located in
                c.setLocation(getLocation(nameElement));

                // Get opening hours
                Elements opening = detail.getElementsByClass("opening");
                c.setOpeningHours(constructOpeningHours(opening.get(0).getElementsByTag("tr")));

                // Get additional information (e.g. evening mensa)
                c.setAdditional(opening.get(0).getElementsByTag("p").text());

                // Get Link
                c.setLinkToFoodPlan(new URL("https://www.studentenwerk-wuerzburg.de" + detail.getElementsByClass("fi").attr("href")));

                System.out.println(c);
            }
        }
    }

    // TODO
    private List<OpeningHours> constructOpeningHours(Elements contentTableRows) {
        List<OpeningHours> openingHoursList = new ArrayList<>();
        int weekdayCounter = 0;
        for (Element tableRow : contentTableRows) {
            Elements tableRowItems = tableRow.children();

            // Do we have opening hours?
            if (tableRowItems.size() > 2) {
                String[] hours = tableRowItems.get(1).text().split(" - | ");
                String mealOutTill = tableRowItems.get(2).text().split(" ")[2];
                int numberOfLoopExecutions = weekdaysToIteratorNumber(tableRowItems.get(0).text());

                for (int i = 0; i < numberOfLoopExecutions; i++) {
                    OpeningHours h = new OpeningHours();
                    h.setWeekday(Weekday.values()[weekdayCounter]);
                    h.setOpen(true);
                    if (hours.length > 0) {
                        h.setOpeningAt(hours[0]);
                        h.setClosingAt(hours[1]);
                    }
                    h.setGetAMealTill(mealOutTill);
                    weekdayCounter++;
                    openingHoursList.add(h);
                }
            }
        }
        return openingHoursList;
    }

    private Location getLocation(String nameElement) {
        if (nameElement.contains("Würzburg"))
            return Location.Würzburg;
        if (nameElement.contains("Bamberg"))
            return Location.Bamberg;
        if (nameElement.contains("Aschaffenburg"))
            return Location.Aschaffenburg;
        if (nameElement.contains("Schweinfurt"))
            return Location.Schweinfurt;

        return null;
    }

    private int weekdaysToIteratorNumber(String weekdays) {
        if (weekdays.equals("Mo-Fr"))
            return 5;
        if (weekdays.length() == 2)
            return 1;

        return 0;
    }
}
