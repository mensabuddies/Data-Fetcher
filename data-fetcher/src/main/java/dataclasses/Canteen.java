package dataclasses;

import java.net.URL;
import java.util.List;

public class Canteen {
    private String name;
    private Location location;
    private String info;
    private List<OpeningHours> openingHours;
    private String additional;
    private URL linkToFoodPlan;

    public Canteen() {
        // Empty constructor
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public List<OpeningHours> getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(List<OpeningHours> openingHours) {
        this.openingHours = openingHours;
    }

    public URL getLinkToFoodPlan() {
        return linkToFoodPlan;
    }

    public void setLinkToFoodPlan(URL linkToFoodPlan) {
        this.linkToFoodPlan = linkToFoodPlan;
    }

    public String getAdditional() {
        return additional;
    }

    public void setAdditional(String additional) {
        this.additional = additional;
    }

    @Override
    public String toString() {
        return "Canteen{" +
                "name='" + name + '\'' +
                ", location=" + location +
                ", info='" + info + '\'' +
                ", openingHours=" + openingHours +
                ", additional='" + additional + '\'' +
                ", linkToFoodPlan=" + linkToFoodPlan +
                '}';
    }
}
