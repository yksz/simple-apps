package weather.api;


/**
 * This interface is the API to get the weather forecast.
 */
public interface WeatherAPI {

    /**
     * Get the weather forecast for the next few days in the specified location.
     *
     * @param location the specified location
     * @return the weather forecast
     * @throws WeatherAPIException if the weather forecast can not be get
     * @throws NullPointerException if location is null
     * @throws IllegalArgumentException if location is empty
     */
    Forecast[] getForecast(String location) throws WeatherAPIException;

}
