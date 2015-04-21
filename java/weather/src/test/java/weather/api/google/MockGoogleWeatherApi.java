package weather.api.google;

import java.io.InputStream;

import weather.api.Forecast;
import weather.api.WeatherApiException;

public class MockGoogleWeatherApi extends GoogleWeatherApi {

    private static final String RESPONSE_FILE = "google.xml";

    @Override
    public Forecast[] getForecast(String location) throws WeatherApiException {
        ClassLoader classLoader = getClass().getClassLoader();
        try (InputStream in = classLoader.getResourceAsStream(RESPONSE_FILE)) {
            if (in == null)
                throw new WeatherApiException("Could not be found: " + RESPONSE_FILE);
            return super.parse(in);
        } catch (Exception e) {
            throw new WeatherApiException(e);
        }
    }

}
