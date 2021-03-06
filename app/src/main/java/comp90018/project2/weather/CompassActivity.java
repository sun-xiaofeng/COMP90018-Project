package comp90018.project2.weather;

import android.content.Context;
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
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * The CompassActivity is an Activity of a digital compass.
 */
public class CompassActivity extends AppCompatActivity implements SensorEventListener {
    /** The compass image view */
    private ImageView compassImageView;
    /** The degrees text view */
    private TextView degreesTextView;

    /** The accelerometer sensor data */
    private float[] mGravity = new float[3];
    /** The megametre sensor data */
    private float[] mGeomagnetic = new float[3];
    private float currentDegree = 0f;

    /***/
    private SensorManager mSensorManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);

        compassImageView = (ImageView) findViewById(R.id.back);
        degreesTextView = (TextView) findViewById(R.id.degrees);


        Toolbar toolbar = (Toolbar) findViewById(R.id.compass_toolbar);
        setSupportActionBar(toolbar);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        final float alpha = 0.8f;
        synchronized (this) {
            if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                mGravity[0] = alpha * mGravity[0] + (1 - alpha) * sensorEvent.values[0];
                mGravity[1] = alpha * mGravity[1] + (1 - alpha) * sensorEvent.values[1];
                mGravity[2] = alpha * mGravity[2] + (1 - alpha) * sensorEvent.values[2];
            }
            if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                mGeomagnetic[0] = alpha * mGeomagnetic[0] + (1 - alpha) * sensorEvent.values[0];
                mGeomagnetic[1] = alpha * mGeomagnetic[1] + (1 - alpha) * sensorEvent.values[1];
                mGeomagnetic[2] = alpha * mGeomagnetic[2] + (1 - alpha) * sensorEvent.values[2];
            }
            float[] R = new float[9];
            float[] I = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
            if (success) {
                updateCompassOrientation(R);
            }
        }
    }

    /**
     * Updates the orientation of the compass according to the rotation matrix
     * @param R the rotation matrix
     */
    private void updateCompassOrientation(float[] R) {
        float[] orientation = new float[3];
        SensorManager.getOrientation(R, orientation);
        float degree = (float) Math.toDegrees(orientation[0]);
        degree = (degree + 360) % 360;
        String degreeText = Integer.toString(Math.round(degree)) + (char) 0x00B0;
        degreesTextView.setText(degreeText);

        // Creates a rotate animation
        Animation rotateAnimation = new RotateAnimation(-currentDegree, -degree, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        currentDegree = degree;
        rotateAnimation.setDuration(1000);
        rotateAnimation.setRepeatCount(0);
        // Rotate the image
        compassImageView.setAnimation(rotateAnimation);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        menu.findItem(R.id.compassItem).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.weatherItem:
                startWeatherActivity();
                return true;
            case R.id.stepCounterItem:
                startStepCounterActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void startWeatherActivity() {
        Intent intent = new Intent(CompassActivity.this, WeatherActivity.class);
        startActivity(intent);
    }

    private void startStepCounterActivity() {
        Intent intent = new Intent(CompassActivity.this, StepCounterActivity.class);
        startActivity(intent);
    }
}
