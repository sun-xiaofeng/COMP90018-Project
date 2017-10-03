package comp90018.project2.weather.data;

import com.google.common.base.Optional;

public class WeatherWarnings {

    public static Optional<String> getWarningMessage(Item item) {
        Condition condition = item.getForecast()[0];
        int code = condition.getCode();
        switch (code) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 17:
            case 23:
            case 37:
            case 38:
            case 39:
            case 45:
            case 47:
                return Optional.of(condition.getDescription() + " today. Please be careful and stay indoors!");
            default:
                return Optional.absent();
        }
    }
}
