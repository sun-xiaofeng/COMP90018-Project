package comp90018.project2.weather;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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

import comp90018.project2.weather.data.Channel;
import comp90018.project2.weather.data.Item;
import comp90018.project2.weather.service.WeatherServiceCallback;
import comp90018.project2.weather.service.YahooWeatherService;

public class WeatherActivity extends AppCompatActivity implements WeatherServiceCallback {

    private ImageView weatherIconImageView;
    private TextView temperatureTextView;
    private TextView conditionTextView;
    private TextView locationTextView;
    private EditText searchEditText;
    private Button switchButton;

    private YahooWeatherService service;
    private ProgressDialog dialog;

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
                    updateLocation();
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

        service = new YahooWeatherService(this);
        dialog = new ProgressDialog(this);
        showLoadingDialog();
        service.refreshWeather("Melbourne, VIC");
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
        locationTextView.setText(service.getLocation());
    }

    @Override
    public void serviceFailure(Exception ex) {
        dialog.hide();
        Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
    }

    public void updateLocation() {
        String location = searchEditText.getText().toString();
        showLoadingDialog();
        service.refreshWeather(location);
    }

    public void showLoadingDialog() {
        dialog.setMessage("Loading...");
        dialog.show();
    }

}
