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

import comp90018.project2.weather.data.LocationResult;
import comp90018.project2.weather.listener.LocationServiceListener;

/**
 * Created by sunxiaofeng208 on 2017/9/19.
 */

public class LocationService {
    private static final String TAG = LocationService.class.getName();

    private Context context;
    private LocationServiceListener listener;
    private Exception error;

    public LocationService(Context context, LocationServiceListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void refreshLocation(Location location) {
        new AsyncTask<Location, Void, LocationResult>() {
            @Override
            protected LocationResult doInBackground(Location... locations) {
                Location location = locations[0];
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                Log.d(TAG, String.format("Location: (%f, %f)", latitude, longitude));
                Geocoder geocoder = new Geocoder(context, Locale.ENGLISH);
                try {
                    List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
                    if (addressList.size() > 0) {
                        Address address = addressList.get(0);
                        return new LocationResult(address.getLocality() + ", " + address.getAdminArea());
                    }
                } catch (IOException ex) {
                    Log.e(TAG, ex.toString());
                    error = ex;
                }
                return null;
            }

            @Override
            protected void onPostExecute(LocationResult location) {
                if (location == null || error != null) {
                    listener.geocodeFailure(error);
                } else {
                    Log.d(TAG, "Success: " + location.getAddress());
                    listener.geocodeSuccess(location);
                }
            }
        }.execute(location);

    }
}
