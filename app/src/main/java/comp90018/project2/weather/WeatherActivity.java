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
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import comp90018.project2.weather.data.Channel;
import comp90018.project2.weather.data.Condition;
import comp90018.project2.weather.data.Item;
import comp90018.project2.weather.service.GeocodingResult;
import comp90018.project2.weather.data.Units;
import comp90018.project2.weather.fragments.ForecastFragment;
import comp90018.project2.weather.listener.GeocodingServiceListener;
import comp90018.project2.weather.service.GeocodingService;
import comp90018.project2.weather.service.WeatherServiceCallback;
import comp90018.project2.weather.service.YahooWeatherService;


public class WeatherActivity extends AppCompatActivity implements WeatherServiceCallback, LocationListener, GeocodingServiceListener {

    public static final int LOCATION_REQUEST_CODE = 1;
    public static final int FORECAST_DAYS = 5;

    private ImageView weatherIconImageView;
    private TextView temperatureTextView;
    private TextView conditionTextView;
    private TextView locationTextView;
    private EditText searchEditText;
    private Button switchButton;
    private ImageButton locationButton;

    private YahooWeatherService weatherService;
    private GeocodingService geocodingService;
    private ProgressDialog dialog;

    private ForecastFragment[] fragments;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        weatherIconImageView = (ImageView) findViewById(R.id.weatherIconImageView);
        temperatureTextView = (TextView) findViewById(R.id.temperatureTextView);
        conditionTextView = (TextView) findViewById(R.id.conditionTextView);
        locationTextView = (TextView) findViewById(R.id.locationTextView);
        searchEditText = (EditText) findViewById(R.id.editText);

        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    getWeatherBySearch(view.getText().toString().trim());
                    view.setText("");
                }
                return false;
            }
        });

        switchButton = (Button) findViewById(R.id.toCompassButton);
        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WeatherActivity.this, CompassActivity.class);
                startActivity(intent);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        locationButton = (ImageButton) findViewById(R.id.locationButton);
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoadingDialog();
                getWeatherForCurrentLocation();
            }
        });


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
        String locationCache = preferences.getString("location_cache", null);
        if (locationCache != null) {
            weatherService.refreshWeather(locationCache);
        } else  {
            getWeatherForCurrentLocation();
        }
    }

    @Override
    public void serviceSuccess(Channel channel) {
        dialog.hide();
        Item item = channel.getItem();
        Units units = channel.getUnits();
        int resourceId = getResources().getIdentifier("drawable/icon_" + item.getCondition().getCode(),
                null, getPackageName());
        weatherIconImageView.setImageResource(resourceId);
        temperatureTextView.setText(item.getCondition().getTemperature() + "\u00B0" + units.getTemperature());
        conditionTextView.setText(item.getCondition().getDescription());
        locationTextView.setText(channel.getLocation().toString());

        Condition[] forecast = item.getForecast();
        for (int i = 0; i < FORECAST_DAYS; i++) {
            fragments[i].loadWeatherForecast(forecast[i], units);
        }
    }

    @Override
    public void serviceFailure(Exception ex) {
        dialog.hide();
        Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
    }

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

    private void getWeatherBySearch(String location) {
        if (!location.isEmpty()) {
            if (!isValidLocation(location)) {
                Toast.makeText(this, "Invalid location!", Toast.LENGTH_SHORT).show();
            } else {
                showLoadingDialog();
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("location_cache", location);
                editor.apply();
                weatherService.refreshWeather(location);
            }
        }
    }

    private void getWeatherForCurrentLocation() {
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

            if (isNetworkEnabled) {
                locationCriteria.setAccuracy(Criteria.ACCURACY_COARSE);
            } else if (isGPSEnabled) {
                locationCriteria.setAccuracy(Criteria.ACCURACY_FINE);
            }
            locationManager.requestSingleUpdate(locationCriteria, this, null);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
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
        getMenuInflater().inflate(R.menu.popup_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private boolean isValidLocation(String location) {
        return location.matches("[a-zA-Z]+(, [a-zA-Z]+)?");
    }
}
