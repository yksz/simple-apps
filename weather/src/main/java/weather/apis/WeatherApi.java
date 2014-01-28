package weather.apis;

import weather.Weather;
import weather.WeatherException;

/**
 * This interface is an api to get the weather forecast.
 */
public interface WeatherApi {

    /**
     * Get the weather forecast for the next few days in the specified location.
     *
     * @param location the specified location
     * @return the weather forecast
     * @throws WeatherException if the weather forcast can not be got
     * @throws NullPointerException if location is null
     * @throws IllegalArgumentException if location is empty
     */
    Weather[] getWeather(String location) throws WeatherException;

}
