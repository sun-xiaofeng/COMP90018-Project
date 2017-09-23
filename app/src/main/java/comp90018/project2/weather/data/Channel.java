package comp90018.project2.weather.data;

import org.json.JSONObject;


public class Channel implements JSONPopulator {
    private Units units;
    private Location location;
    private Item item;

    public Units getUnits() {
        return units;
    }

    public Item getItem() {
        return item;
    }

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
