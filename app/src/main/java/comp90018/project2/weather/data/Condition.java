package comp90018.project2.weather.data;

import org.json.JSONObject;

public class Condition implements JSONPopulator {
    private int code;
    private int temperature;
    private int high;
    private int low;
    private String description;
    private String day;

    public int getCode() {
        return code;
    }

    public int getTemperature() {
        return temperature;
    }

    public String getDescription() {
        return description;
    }

    public String getDay() {
        return day;
    }

    public int getHighTemperature() {
        return high;
    }

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
