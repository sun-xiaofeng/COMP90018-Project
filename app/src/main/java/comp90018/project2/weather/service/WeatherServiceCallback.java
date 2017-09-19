package comp90018.project2.weather.service;

import comp90018.project2.weather.data.Channel;

/**
 * Created by sunxiaofeng208 on 2017/9/12.
 */

public interface WeatherServiceCallback {
    void serviceSuccess(Channel channel);
    void serviceFailure(Exception ex);
}
