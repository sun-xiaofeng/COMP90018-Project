package comp90018.project2.weather.data;

import org.json.JSONObject;

/**
 * Created by sunxiaofeng208 on 2017/9/12.
 */

public class Channel implements JSONPopulator {
    private Units units;
    private Item item;
    private String location;

    public Units getUnits() {
        return units;
    }

    public Item getItem() {
        return item;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public void populate(JSONObject data) {
        units = new Units();
        units.populate(data.optJSONObject("units"));
        item = new Item();
        item.populate(data.optJSONObject("item"));

        JSONObject locationData = data.optJSONObject("location");

        String region = locationData.optString("region");
        String country = locationData.optString("country");

        location = String.format("%s, %s", locationData.optString("city"), (region.length() != 0 ? region : country));
    }
}
