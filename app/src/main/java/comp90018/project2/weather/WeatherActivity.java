package comp90018.project2.weather;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.base.Optional;

import comp90018.project2.weather.data.BackgroundImages;
import comp90018.project2.weather.data.Channel;
import comp90018.project2.weather.data.Condition;
import comp90018.project2.weather.data.Item;
import comp90018.project2.weather.data.WeatherWarnings;
import comp90018.project2.weather.service.GeocodingResult;
import comp90018.project2.weather.data.Units;
import comp90018.project2.weather.fragments.ForecastFragment;
import comp90018.project2.weather.listener.GeocodingServiceListener;
import comp90018.project2.weather.service.GeocodingService;
import comp90018.project2.weather.listener.WeatherServiceListener;
import comp90018.project2.weather.service.YahooWeatherService;

/**
 * The main Activity. It shows the weather forecast and
 * warning messages for severe weather conditions.
 */
public class WeatherActivity extends AppCompatActivity implements WeatherServiceListener,
        LocationListener, GeocodingServiceListener {

    private static final int LOCATION_REQUEST_CODE = 1;
    private static final int FORECAST_DAYS = 5;

    /** The UI elements*/
    private ImageView backgroundImageView;
    private ImageView weatherIconImageView;

    private TextView temperatureTextView;
    private TextView conditionTextView;
    private TextView locationTextView;
    private TextView weatherDescriptionTextView;
    private TextView weatherWarningTextView;

    private View weatherWarningLayout;
    private ProgressDialog dialog;
    private ForecastFragment[] fragments;

    /** The SharedPreferences, using for location cache */
    private SharedPreferences preferences;

    /** The Yahoo weather service */
    private YahooWeatherService weatherService;
    /** The Geocoding service */
    private GeocodingService geocodingService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        backgroundImageView = (ImageView) findViewById(R.id.background);
        weatherIconImageView = (ImageView) findViewById(R.id.weatherIconImageView);
        temperatureTextView = (TextView) findViewById(R.id.temperatureTextView);
        conditionTextView = (TextView) findViewById(R.id.conditionTextView);
        locationTextView = (TextView) findViewById(R.id.locationTextView);
        weatherDescriptionTextView = (TextView) findViewById(R.id.weatherDescriptionTextView);
        weatherWarningTextView = (TextView) findViewById(R.id.weatherWarningTextView);
        weatherWarningLayout = findViewById(R.id.weatherWarningLayout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.weather_toolbar);
        setSupportActionBar(toolbar);


        fragments = new ForecastFragment[FORECAST_DAYS];
        fragments[0] = (ForecastFragment) getSupportFragmentManager().findFragmentById(R.id.forecastDay1);
        fragments[1] = (ForecastFragment) getSupportFragmentManager().findFragmentById(R.id.forecastDay2);
        fragments[2] = (ForecastFragment) getSupportFragmentManager().findFragmentById(R.id.forecastDay3);
        fragments[3] = (ForecastFragment) getSupportFragmentManager().findFragmentById(R.id.forecastDay4);
        fragments[4] = (ForecastFragment) getSupportFragmentManager().findFragmentById(R.id.forecastDay5);

        preferences = getSharedPreferences("MyPref", 0);

        weatherService = new YahooWeatherService(this);
        geocodingService = new GeocodingService(this, this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        dialog = new ProgressDialog(this);
        showLoadingDialog();

        String loc = getIntent().getStringExtra("location");
        if (loc != null) { // Use location from Intent
           weatherService.refreshWeather(loc);
        } else {
            String locationCache = preferences.getString("location_cache", null);
            if (locationCache != null) { // Use location from cache
                weatherService.refreshWeather(locationCache);
            } else  { // Use current location
                getWeatherForCurrentLocation();
            }
        }
    }

    @Override
    public void weatherServiceSuccess(Channel channel) {
        dialog.hide();
        showCurrentWeather(channel);
        showWeatherWarning(channel);
        showForecast(channel);
        setBackgroundImage(channel);
    }

    /**
     * Shows current weather including temperature, condition, location
     * and weather description
     * @param channel the channel
     */
    private void showCurrentWeather(Channel channel) {
        Item item = channel.getItem();
        Condition condition = item.getCondition();
        int resourceId = getResources().getIdentifier("drawable/icon_"
                        + condition.getCode(), null, getPackageName());
        weatherIconImageView.setImageResource(resourceId);
        String temperatureText = condition.getTemperature() + "\u00B0";
        temperatureTextView.setText(temperatureText);
        conditionTextView.setText(condition.getDescription());
        locationTextView.setText(channel.getLocation().toString());
        weatherDescriptionTextView.setText(item.toString());
    }

    /**
     * Shows warning messages for severe weather
     * @param channel the channel
     */
    private void showWeatherWarning(Channel channel) {
        Item item = channel.getItem();
        Optional<String> warningMessageOptional = WeatherWarnings.getWarningMessage(item);
        if (warningMessageOptional.isPresent()) {
            final String textColor = "#8B0000";
            @SuppressWarnings("deprecation")
            CharSequence warningMessage = Html.fromHtml("<b><font color=" + textColor
                    + ">Warning: </font></b>" + warningMessageOptional.get());
            weatherWarningTextView.setText(warningMessage);
            weatherWarningTextView.setVisibility(View.VISIBLE);
            weatherWarningLayout.setVisibility(View.VISIBLE);
        } else {
            weatherWarningTextView.setVisibility(View.GONE);
            weatherWarningLayout.setVisibility(View.GONE);
        }
    }

    /**
     * Displays the weather forecast for 5 days
     * @param channel the channel
     */
    private void showForecast(Channel channel) {
        Condition[] forecast = channel.getItem().getForecast();
        Units units = channel.getUnits();
        for (int i = 0; i < FORECAST_DAYS; i++) {
            fragments[i].loadWeatherForecast(forecast[i], units);
        }
    }

    /**
     * Sets background image according to the weather
     * @param channel the channel
     */
    private void setBackgroundImage(Channel channel) {
        int resId = BackgroundImages.getBackgroundImage(channel.getItem());
        backgroundImageView.setImageResource(resId);
    }

    @Override
    public void weatherServiceFailure(Exception ex) {
        dialog.hide();
        Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
    }

    /**
     * Displays the loading dialog
     */
    private void showLoadingDialog() {
        dialog.setMessage("Loading...");
        dialog.show();
    }

    @Override
    public void geocodeSuccess(GeocodingResult locationResult) {
        String location = locationResult.getAddress();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("location_cache", location);
        editor.apply();
        weatherService.refreshWeather(location);
    }

    @Override
    public void geocodeFailure(Exception exception) {
        dialog.hide();
        Toast.makeText(this, exception.getMessage(), Toast.LENGTH_LONG).show();
    }

    /**
     * Retrieves weather forecast for user input location
     * @param location the search string
     */
    private void getWeatherBySearch(String location) {
        String regex = "[a-zA-Z ]+(, ?[a-zA-Z ]+)?";
        if (!location.isEmpty()) {
            if (!location.matches(regex)) { // Check whether the input only contains letters and comma
                Toast.makeText(this, "Invalid location!", Toast.LENGTH_SHORT).show();
            } else {
                showLoadingDialog();
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("location_cache", location); // Store the location into the cache
                editor.apply();
                weatherService.refreshWeather(location);
            }
        }
    }

    /**
     * Retrieves weather forecast for current location
     */
    private void getWeatherForCurrentLocation() {
        // Check the location permission
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
            }, LOCATION_REQUEST_CODE);
        } else {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            Criteria locationCriteria = new Criteria();

            if (isNetworkEnabled) { // Use network location which is faster
                locationCriteria.setAccuracy(Criteria.ACCURACY_COARSE);
            } else if (isGPSEnabled) { // Otherwise use GPS
                locationCriteria.setAccuracy(Criteria.ACCURACY_FINE);
            }
            locationManager.requestSingleUpdate(locationCriteria, this, null);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getWeatherForCurrentLocation();
                } else {
                    dialog.hide();
                    Toast.makeText(this, "Location permission denied!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        geocodingService.refreshLocation(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        menu.findItem(R.id.weatherItem).setVisible(false);
        menu.findItem(R.id.locationItem).setVisible(true);
        menu.findItem(R.id.searchItem).setVisible(true);
        menu.findItem(R.id.locationListItem).setVisible(true);

        final MenuItem searchMenuItem = menu.findItem(R.id.searchItem);
        final SearchView searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.setIconified(true);
                searchView.clearFocus();
                getWeatherBySearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.locationItem:
                showLoadingDialog();
                getWeatherForCurrentLocation();
                return true;
            case R.id.locationListItem:
                startLocationListActivity();
                return true;
            case R.id.stepCounterItem:
                startStepCounterActivity();
                return true;
            case R.id.compassItem:
                startCompassActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Switches to compass activity
     */
    private void startCompassActivity() {
        Intent intent = new Intent(WeatherActivity.this, CompassActivity.class);
        startActivity(intent);
    }

    /**
     * Switches to step counter activity
     */
    private void startStepCounterActivity() {
        Intent intent = new Intent(WeatherActivity.this, StepCounterActivity.class);
        startActivity(intent);
    }

    /**
     * Switches to location list activity
     */
    private void startLocationListActivity() {
        Intent intent = new Intent(WeatherActivity.this, LocationListActivity.class);
        startActivity(intent);
    }
}
