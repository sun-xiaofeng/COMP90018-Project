package comp90018.project2.weather.data;

import com.google.common.base.Optional;

public class WeatherWarnings {

    public static Optional<String> getWarningMessage(Item item) {
        Condition condition = item.getForecast()[0];
        int code = condition.getCode();
        StringBuilder message = new StringBuilder();
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
                message.append(condition.getDescription());
                message.append(" today. Please be careful and stay indoors! ");
                break;
            case 8: // Freezing drizzle
            case 9: // Drizzle
            case 10: // Freezing rain
            case 11: // Showers
            case 12: // Showers
            case 35: // Mixed rain and hail
            case 40: // Scattered showers
                message.append(condition.getDescription());
                message.append(". Please bring an umbrella or raincoat. ");
                break;
            default:
                break;
        }

        if (condition.getHighTemperature() > 32) {
            message.append("High temperature. Drink more water to prevent heat stroke. ");
        } else if (condition.getLowTemperature() < 0) {
            message.append("Low temperature. Dress warmly if you're going out. ");
        }
        if (message.length() > 0) {
            return Optional.of(message.toString());
        }
        return Optional.absent();
    }
}
