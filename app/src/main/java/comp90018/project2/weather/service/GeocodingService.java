package comp90018.project2.weather.service;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import comp90018.project2.weather.listener.GeocodingServiceListener;


public class GeocodingService {
    private Context context;
    private GeocodingServiceListener listener;
    private Exception exception;

    public GeocodingService(Context context, GeocodingServiceListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void refreshLocation(Location location) {
        new AsyncTask<Location, Void, GeocodingResult>() {
            @Override
            protected GeocodingResult doInBackground(Location... locations) {
                Location location = locations[0];
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                Geocoder geocoder = new Geocoder(context, Locale.ENGLISH);
                try {
                    List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
                    if (addressList.size() > 0) {
                        Address address = addressList.get(0);
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
