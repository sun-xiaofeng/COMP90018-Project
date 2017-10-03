package comp90018.project2.weather.service;

import comp90018.project2.weather.data.Channel;


public interface WeatherServiceCallback {
    void weatherServiceSuccess(Channel channel);
    void weatherServiceFailure(Exception ex);
}
