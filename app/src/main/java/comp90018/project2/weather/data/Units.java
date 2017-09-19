package comp90018.project2.weather.data;

import org.json.JSONObject;

/**
 * Created by sunxiaofeng208 on 2017/9/12.
 */

public class Units implements JSONPopulator {
    private String temperature;

    public String getTemperature() {
        return temperature;
    }

    public void populate(JSONObject data) {
        temperature = data.optString("temperature");
    }
}
