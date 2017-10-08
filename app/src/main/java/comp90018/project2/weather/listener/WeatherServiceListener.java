package comp90018.project2.weather.listener;

import comp90018.project2.weather.data.Channel;


/**
 * The listener interface for receiving weather service result
 */
public interface WeatherServiceListener {
    /**
     * Invokes when weather service succeed.
     * @param channel the channel
     */
    void weatherServiceSuccess(Channel channel);

    /**
     * Invokes when weather service failed.
     * @param ex the error
     */
    void weatherServiceFailure(Exception ex);
}
