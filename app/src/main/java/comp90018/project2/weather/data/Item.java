package comp90018.project2.weather.data;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * A Item contains the local weather conditions and forecast for a specific location.
 */
public class Item implements JSONPopulator {
    /* The current weather condition */
    private Condition condition;
    /** The forecast */
    private Condition[] forecast;

    /**
     * Returns the current weather condition
     * @return the current condition
     */
    public Condition getCondition() {
        return condition;
    }

    /**
     * Returns the forecast
     * @return the forecast
     */
    public Condition[] getForecast() {
        return forecast;
    }

    @Override
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
