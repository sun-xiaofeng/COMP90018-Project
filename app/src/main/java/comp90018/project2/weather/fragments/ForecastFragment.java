package comp90018.project2.weather.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import comp90018.project2.weather.R;
import comp90018.project2.weather.data.Condition;
import comp90018.project2.weather.data.Units;


public class ForecastFragment extends Fragment {
    private ImageView weatherIconImageView;
    private TextView dayLabelTextView;
    private TextView highTemperatureTextView;
    private TextView lowTemperatureTextView;

    public ForecastFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather_condition, container, false);
        weatherIconImageView = (ImageView) view.findViewById(R.id.weatherIconImageView);
        dayLabelTextView = (TextView) view.findViewById(R.id.dayTextView);
        highTemperatureTextView = (TextView) view.findViewById(R.id.highTemperatureTextView);
        lowTemperatureTextView = (TextView) view.findViewById(R.id.lowTemperatureTextView);
        return view;
    }

    public void loadWeatherForecast(Condition forecast, Units unit) {
        Log.d("ForecastFragment", forecast.getCode() + "");
        int resourceId = getResources().getIdentifier("drawable/icon_" + forecast.getCode(),
                null, getActivity().getPackageName());
        weatherIconImageView.setImageResource(resourceId);
        dayLabelTextView.setText(forecast.getDay());
        String highTemperatureText = forecast.getHighTemperature() + "\u00B0";
        String lowTemperatureText = forecast.getLowTemperature() + "\u00B0";
        highTemperatureTextView.setText(highTemperatureText);
        lowTemperatureTextView.setText(lowTemperatureText);
    }

}
