package comp90018.project2.weather;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Stack;

import comp90018.project2.weather.data.Channel;
import comp90018.project2.weather.data.Item;
import comp90018.project2.weather.data.LocationResult;
import comp90018.project2.weather.listener.LocationServiceListener;
import comp90018.project2.weather.service.LocationService;
import comp90018.project2.weather.service.WeatherServiceCallback;
import comp90018.project2.weather.service.YahooWeatherService;

import static android.R.id.input;
import static comp90018.project2.weather.R.string.location;

public class WeatherActivity extends AppCompatActivity implements WeatherServiceCallback, LocationListener, LocationServiceListener {

    public static final int REQUEST_LOCATION = 1;

    private ImageView weatherIconImageView;
    private TextView temperatureTextView;
    private TextView conditionTextView;
    private TextView locationTextView;
    private EditText searchEditText;
    private Button switchButton;

    private YahooWeatherService weatherService;
    private LocationService locationService;
    private ProgressDialog dialog;

    private Stack<String> previousLocations = new Stack<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        weatherIconImageView = (ImageView) findViewById(R.id.weatherIconImageView);
        temperatureTextView = (TextView) findViewById(R.id.temperature_text_view);
        conditionTextView = (TextView) findViewById(R.id.condition_text_view);
        locationTextView = (TextView) findViewById(R.id.location_text_view);
        searchEditText = (EditText) findViewById(R.id.editText);

        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    getWeatherBySearch(v.getText().toString());
                    v.setText("");
                }
                return false;
            }
        });

        switchButton = (Button) findViewById(R.id.toCompassButton);
        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WeatherActivity.this, CompassActivity.class);
                startActivity(intent);
            }
        });

        weatherService = new YahooWeatherService(this);
        locationService = new LocationService(this, this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        dialog = new ProgressDialog(this);
        showLoadingDialog();
        if (!previousLocations.isEmpty()) {
            String location = previousLocations.peek();
            weatherService.refreshWeather(location);
        } else {
            getWeatherFromCurrentLocation();
        }
    }

    @Override
    public void serviceSuccess(Channel channel) {
        dialog.hide();
        Item item = channel.getItem();
        int resourceId = getResources().getIdentifier("drawable/icon_" + item.getCondition().getCode(), null, getPackageName());
        @SuppressWarnings("deprecation")
        Drawable weatherIconDrawable = getResources().getDrawable(resourceId);

        weatherIconImageView.setImageDrawable(weatherIconDrawable);
        temperatureTextView.setText(item.getCondition().getTemperature() + "\u00B0" + channel.getUnits().getTemperature());
        conditionTextView.setText(item.getCondition().getDescription());
        locationTextView.setText(weatherService.getLocation());
    }

    @Override
    public void serviceFailure(Exception ex) {
        dialog.hide();
        Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
    }

    private void getWeatherBySearch(String inputText) {
        inputText = inputText.trim();
        if (!inputText.isEmpty()) {
            if (!isValidLocation(inputText)) {
                Toast.makeText(this, "Invalid location!", Toast.LENGTH_SHORT).show();
            } else {
                showLoadingDialog();
                weatherService.refreshWeather(inputText);
            }
        }
    }

    private boolean isValidLocation(String location) {
        return location.matches("[a-zA-Z]+(, [a-zA-Z]+)?");
    }

    private void showLoadingDialog() {
        dialog.setMessage("Loading...");
        dialog.show();
    }

    @Override
    public void geocodeSuccess(LocationResult locationResult) {
        String location = locationResult.getAddress();
        previousLocations.push(location);
        weatherService.refreshWeather(location);
    }

    @Override
    public void geocodeFailure(Exception exception) {
        dialog.hide();
        Toast.makeText(this, exception.getMessage(), Toast.LENGTH_LONG).show();
    }

    private void getWeatherFromCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
            }, REQUEST_LOCATION);
        } else {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, this, null);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION:
                getWeatherFromCurrentLocation();
                break;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        locationService.refreshLocation(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }
}
