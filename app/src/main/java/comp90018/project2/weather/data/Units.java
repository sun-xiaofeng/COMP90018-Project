package comp90018.project2.weather.data;

import org.json.JSONObject;


public class Units implements JSONPopulator {
    private String temperature;

    public String getTemperature() {
        return temperature;
    }

    public void populate(JSONObject data) {
        temperature = data.optString("temperature");
    }
}
