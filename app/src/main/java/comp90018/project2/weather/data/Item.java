package comp90018.project2.weather.data;

import org.json.JSONArray;
import org.json.JSONObject;

public class Item implements JSONPopulator {
    private Condition condition;
    private Condition[] forecast;

    public Condition getCondition() {
        return condition;
    }

    public Condition[] getForecast() {
        return forecast;
    }

    public void populate(JSONObject data) {
        condition = new Condition();
        condition.populate(data.optJSONObject("condition"));
        JSONArray forecastData = data.optJSONArray("forecast");

        forecast = new Condition[forecastData.length()];

        for (int i = 0; i < forecastData.length(); i++) {
            forecast[i] = new Condition();
            forecast[i].populate(forecastData.optJSONObject(i));
        }
    }

    @Override
    public String toString() {
        return "Today is " + forecast[0].getDescription() + ". It's currently "
                + condition.getTemperature() + "\u00B0" + ". The high will be "
                + forecast[0].getHighTemperature() + "\u00B0" + ".";
    }
}
