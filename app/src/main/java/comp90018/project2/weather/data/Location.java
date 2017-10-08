package comp90018.project2.weather.data;

import org.json.JSONObject;

/**
 * A Location contains city, region and country information of a specific location
 */
public class Location implements JSONPopulator {
    /** The city */
    private String city;
    /** The country */
    private String country;
    /** The region */
    private String region;

    /**
     * Returns the name of the city
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * Returns the name of the country
     * @return the country
     */
    public String getCountry() {
        return country;
    }

    /**
     * Returns the region name
     * @return the region
     */
    public String getRegion() {
        return region;
    }

    @Override
    public String toString() {
        return city + ", " + (!region.isEmpty() ? region : country);
    }

    @Override
    public void populate(JSONObject data) {
        city = data.optString("city");
        country = data.optString("country");
        region = data.optString("region");
    }
}
