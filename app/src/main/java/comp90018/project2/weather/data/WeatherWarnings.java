package comp90018.project2.weather.data;

import com.google.common.base.Optional;

public class WeatherWarnings {

    public static Optional<String> getWarningMessage(Item item) {
        Condition condition = item.getForecast()[0];
        int code = condition.getCode();
        switch (code) {
            case 0: // Tornado
            case 1: // Tropical Storm
            case 2: // Hurricane
            case 3: // Severe Thunderstorms
            case 4: // Thunderstorms
            case 17: // Hail
            case 23: // Blustery
            case 37: // Isolated Thunderstorms
            case 38: // Scattered Thunderstorms
            case 39: // Scattered Thunderstorms
            case 45: // Thundershowers
            case 47: // Isolated Thundershowers
                return Optional.of(condition.getDescription() + " today. Please be careful and stay indoors!");
            default:
                // No warning message
                return Optional.absent();
        }
    }
}
