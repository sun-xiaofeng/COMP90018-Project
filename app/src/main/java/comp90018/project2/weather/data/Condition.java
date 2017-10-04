package comp90018.project2.weather.data;

import org.json.JSONObject;

/**
 * A Condition object contains weather conditions including condition code,
 * current temperature, high temperature, low temperature, description and day.
 */
public class Condition implements JSONPopulator {
    /** The condition code */
    private int code;
    /** The current temperature */
    private int temperature;
    /** The high temperature */
    private int high;
    /** The low temperature */
    private int low;
    /** The weather description */
    private String description;
    /** The day of the week to which this forecast applies */
    private String day;

    /**
     * Returns the condition code
     * @return the condition code
     */
    public int getCode() {
        return code;
    }

    /**
     * Returns the current temperature
     * @return the current temperature
     */
    public int getTemperature() {
        return temperature;
    }

    /**
     * Returns the weather description
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the day of the week
     * @return the day of the week
     */
    public String getDay() {
        return day;
    }

    /**
     * Returns the high temperature
     * @return the high temperature
     */
    public int getHighTemperature() {
        return high;
    }

    /**
     * Returns the low temperature
     * @return the low temperature
     */
    public int getLowTemperature() {
        return low;
    }

    @Override
    public void populate(JSONObject data) {
        code = data.optInt("code");
        temperature = data.optInt("temp");
        high = data.optInt("high");
        low = data.optInt("low");
        description = data.optString("text");
        day = data.optString("day");
    }

}
