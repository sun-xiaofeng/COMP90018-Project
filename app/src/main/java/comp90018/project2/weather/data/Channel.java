package comp90018.project2.weather.data;

import org.json.JSONObject;

/**
 * A Channel object contains units, the location of the forecast and weather conditions.
 */
public class Channel implements JSONPopulator {
    /** The temperature unit */
    private Units units;
    /** The location of the forecast */
    private Location location;
    /** The local weather conditions and forecast for a specific location */
    private Item item;

    /**
     * Returns the units
     * @return the units
     */
    public Units getUnits() {
        return units;
    }

    /**
     * Returns the item that contains weather conditions
     * @return the item
     */
    public Item getItem() {
        return item;
    }

    /**
     * Returns the location of the weather forecast.
     * @return the location
     */
    public Location getLocation() {
        return location;
    }

    @Override
    public void populate(JSONObject data) {
        units = new Units();
        units.populate(data.optJSONObject("units"));
        item = new Item();
        item.populate(data.optJSONObject("item"));
        location = new Location();
        location.populate(data.optJSONObject("location"));
    }
}
