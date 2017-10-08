package comp90018.project2.weather.listener;

import comp90018.project2.weather.service.GeocodingResult;

/**
 * The listener interface for receiving geocoding results.
 */
public interface GeocodingServiceListener {
    /**
     * Invoked when geocoding succeed.
     * @param location the result
     */
    void geocodeSuccess(GeocodingResult location);

    /**
     * Invoked when geocoding failed.
     * @param exception the error
     */
    void geocodeFailure(Exception exception);
}
