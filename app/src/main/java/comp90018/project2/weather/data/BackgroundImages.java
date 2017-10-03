package comp90018.project2.weather.data;

import comp90018.project2.weather.R;


public class BackgroundImages {
    public static int getBackgroundImage(Item item) {
        int code = item.getCondition().getCode();
        switch (code) {
            case 0: // Tornado
            case 1: // Tropical storm
            case 2: // Hurricane
            case 3: // Severe thunderstorms
            case 4: // Thunderstorms
            case 37: // Isolated thunderstorms
            case 38: // Scattered thunderstorms
            case 39: // Scattered thunderstorms
            case 45: // Thundershowers
            case 47: // Isolated thundershowers
                return R.drawable.thunder;
            case 8: // Freezing drizzle
            case 9: // Drizzle
            case 10: // Freezing rain
            case 11: // Showers
            case 12: // Showers
            case 35: // Mixed rain and hail
            case 40: // Scattered showers
                return R.drawable.rainy;
            case 5: // Mixed rain and snow
            case 6: // Mixed rain and sleet
            case 7: // Mixed snow and sleet
            case 13: // Snow flurries
            case 14: // Light snow showers
            case 15: // Blowing snow
            case 16: // Snow
            case 17: // Hail
            case 18: // Sleet
            case 41: // Heavy snow
            case 42: // Scatter snow showers
            case 43: // Heavy snow
            case 46: // Snow showers
                return R.drawable.snow;
            case 31: // Clear (night)
            case 32: // Sunny
            case 33: // Fair (night)
            case 34: // Fair (day)
            case 36: // Hot
                return R.drawable.sunny;
            case 26: // Cloudy
            case 27: // Mostly cloudy (night)
            case 28: // Mostly cloudy (day)
            case 29: // Partly cloudy (night)
            case 30: // Partly cloudy (day)
            case 44: // Partly cloudy
                return R.drawable.cloudy;
            default:
                return R.drawable.background_1;
        }
    }
}
