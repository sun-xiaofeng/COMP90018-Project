package comp90018.project2.weather.listener;

import comp90018.project2.weather.service.GeocodingResult;


public interface GeocodingServiceListener {
    void geocodeSuccess(GeocodingResult location);
    void geocodeFailure(Exception exception);
}
