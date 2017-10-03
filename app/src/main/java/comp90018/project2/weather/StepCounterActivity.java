package comp90018.project2.weather;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import comp90018.project2.weather.service.StatisticsUtil;

public class StepCounterActivity extends AppCompatActivity implements SensorEventListener {

    private TextView stepsInTwoSecondTextView;
    private TextView distanceTextView;
    private TextView speedTextView;
    private TextView stepsTextView;

    private Button show;
    private EditText heightText;

    private SensorManager sm;
    private Sensor accelerometer;

    private List<Double> list;
    private List<Double> bigList;

    private boolean running = false;

    private long startTime, endTime;
    private double time, height, stride, speed, distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_counter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.step_counter_toolbar);
        setSupportActionBar(toolbar);

        stepsTextView = (TextView) findViewById(R.id.Steps);
        show = (Button) findViewById(R.id.show);
        stepsInTwoSecondTextView = (TextView) findViewById(R.id.steps2sec);
        heightText = (EditText) findViewById(R.id.height);
        distanceTextView = (TextView) findViewById(R.id.distance);
        speedTextView = (TextView) findViewById(R.id.speed);

        // Initialize Accelerometer sensor
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        show.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!running) {
                    try {
                        height = Double.parseDouble(heightText.getText().toString()) / 100;
                        running = true;
                        list = new ArrayList<>();
                        bigList = new ArrayList<>();
                        show.setText(R.string.result_button_text);
                        startTime = System.currentTimeMillis();
                        distance = 0;
                        distanceTextView.setText("0");
                        speed = 0;
                        speedTextView.setText("0");
                        stepsTextView.setText("0");
                    } catch (NumberFormatException e) {
                        Toast.makeText(StepCounterActivity.this, "Invalid height!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    running = false;
                    show.setText(R.string.start_button_text);
                    String stepsText = Integer.toString(getSteps(bigList));
                    stepsTextView.setText(stepsText);
                    String distanceText = String.format(Locale.getDefault(), "%.1f m", distance);
                    distanceTextView.setText(distanceText);
                }
            }

        });
    }

    private int getSteps(List<Double> list) {
        StatisticsUtil su = new StatisticsUtil();
        double mean = su.findMean(list);
        double std = su.standardDeviation(list, mean);
        int stepsNumber = su.finAllPeaks(list, std);
        list.clear();
        return stepsNumber;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (running) {
            double x = event.values[0];
            double y = event.values[1];
            // double z = event.values[2];
            // acceleration.setText("X: " + x + "\nY: " + y + "\nZ: " + z);
            // calculate the magnitude mag^2 = x^2 + y^2 + z^2 and add mag to the list
            // we deal with mag due to count stepsTextView in all directions as magnitude neglects directions.
            double mag = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(y, 2));
            list.add(mag);
            bigList.add(mag);
            endTime = System.currentTimeMillis();
            time = (endTime - startTime) / 1000.0;
            if (time >= 2.0) {
                int stepsInTwoSeconds = getSteps(list);
                String stepsInTwoSecondsText = Integer.toString(stepsInTwoSeconds);
                stepsInTwoSecondTextView.setText(stepsInTwoSecondsText);
                if (stepsInTwoSeconds > 0 && stepsInTwoSeconds <= 2) {
                    stride = height / 5;
                } else if (stepsInTwoSeconds > 2 && stepsInTwoSeconds <= 3) {
                    stride = height / 4;
                } else if (stepsInTwoSeconds > 3 && stepsInTwoSeconds <= 4) {
                    stride = height / 3;
                } else if (stepsInTwoSeconds > 4 && stepsInTwoSeconds <= 5) {
                    stride = height / 2;
                } else if (stepsInTwoSeconds > 5 && stepsInTwoSeconds <= 6) {
                    stride = height / 1.2;
                } else if (stepsInTwoSeconds > 6 && stepsInTwoSeconds < 8) {
                    stride = height;
                } else if (stepsInTwoSeconds >= 8) {
                    stride = 1.2 * height;
                }
                distance += stepsInTwoSeconds * stride;
                speed = stepsInTwoSeconds * stride / 2.0;
                String speedText = String.format(Locale.getDefault(), "%.3f m/s", speed);
                speedTextView.setText(speedText);
                startTime = endTime;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        menu.findItem(R.id.stepCounterItem).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.weatherItem:
                startWeatherActivity();
                return true;
            case R.id.compassItem:
                startCompassActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void startCompassActivity() {
        Intent intent = new Intent(StepCounterActivity.this, CompassActivity.class);
        startActivity(intent);
    }

    private void startWeatherActivity() {
        Intent intent = new Intent(StepCounterActivity.this, WeatherActivity.class);
        startActivity(intent);
    }
}
