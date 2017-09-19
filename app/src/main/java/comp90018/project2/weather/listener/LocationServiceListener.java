package comp90018.project2.weather.listener;

import comp90018.project2.weather.data.LocationResult;

/**
 * Created by sunxiaofeng208 on 2017/9/19.
 */

public interface LocationServiceListener {
    void geocodeSuccess(LocationResult location);
    void geocodeFailure(Exception exception);
}
