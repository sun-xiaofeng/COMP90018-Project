package comp90018.project2.weather.data;

import com.google.common.base.Optional;

/**
 * This class provides warning messages according to the weather condition
 */
public class WeatherWarnings {

    /**
     * Returns a warning message according to the weather condition
     * @param item the item
     * @return a warning message
     */
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
            case 23: // Blustery
            case 24: // Windy
                message.append(condition.getDescription());
                message.append(". Please close doors and windows. ");
                break;
            case 19: // Dust
            case 20: // Foggy
            case 21: // Haze
            case 22: // Smoky
                message.append(condition.getDescription());
                message.append(". Be aware on air quality. Please wear masks if you are sensitive.");
                break;
            default:
                break;
        }

        if (condition.getHighTemperature() >= 32) {
            message.append("High temperature. Drink more water to prevent heat stroke. ");
        } else if (condition.getLowTemperature() <= 0) {
            message.append("Low temperature. Dress warmly if you're going out. ");
        }
        if (message.length() > 0) {
            return Optional.of(message.toString());
        }
        return Optional.absent(); // No warning message
    }
}
