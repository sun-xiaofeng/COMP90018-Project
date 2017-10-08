package comp90018.project2.weather.service;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import comp90018.project2.weather.listener.GeocodingServiceListener;

/**
 * The GeocodingService enables conversion of latitude and longitude
 * pairs to addresses
 */
public class GeocodingService {
    private Context context;
    private GeocodingServiceListener listener;
    private Exception exception;

    public GeocodingService(Context context, GeocodingServiceListener listener) {
        this.context = context;
        this.listener = listener;
    }

    /**
     * Converts the latitude and longitude
     * @param location the location
     */
    public void refreshLocation(Location location) {
        new AsyncTask<Location, Void, GeocodingResult>() { // Run the task in background
            @Override
            protected GeocodingResult doInBackground(Location... locations) {
                Location location = locations[0];
                // Retrieve the latitude and longitude
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                Geocoder geocoder = new Geocoder(context, Locale.ENGLISH);
                try {
                    List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
                    if (addressList.size() > 0) {
                        Address address = addressList.get(0);
                        // Return the address, in City, Region format */
                        return new GeocodingResult(address.getLocality() + ", " + address.getAdminArea());
                    }
                } catch (IOException ex) {
                    exception = ex;
                }
                return null;
            }

            @Override
            protected void onPostExecute(GeocodingResult location) {
                if (location == null || exception != null) {
                    listener.geocodeFailure(exception);
                } else {
                    listener.geocodeSuccess(location);
                }
            }
        }.execute(location);
    }
}
