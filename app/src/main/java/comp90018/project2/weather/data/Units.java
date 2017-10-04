package comp90018.project2.weather.data;

import org.json.JSONObject;

/**
 * A Unit object contains the unit for temperature
 */
public class Units implements JSONPopulator {
    private String temperature;

    /**
     * Returns the unit
     * @return the unit
     */
    public String getTemperature() {
        return temperature;
    }

    public void populate(JSONObject data) {
        temperature = data.optString("temperature");
    }
}
