package comp90018.project2.weather.data;

import org.json.JSONObject;

/**
 * A JSONPopulator provides a method to dump values in a JSONObject,
 */
public interface JSONPopulator {
    /**
     * Populates JSON data into fields.
     * @param data
     */
    void populate(JSONObject data);
}
