package weather.api.wunderground;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import weather.api.Forecast;
import weather.api.WeatherApiException;
import weather.widget.util.Loader;

public class MockWeatherUndergroundApi extends WeatherUndergroundApi {

    private static final String RESPONSE_FILE = "wunderground.json";

    public MockWeatherUndergroundApi(String key) {
        super(key);
    }

    @Override
    public Forecast[] getForecast(String location) throws WeatherApiException {
        InputStream in;
        try {
            File file = Loader.getResourceAsFile(RESPONSE_FILE);
            in = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new WeatherApiException(e);
        }

        try {
            return super.parse(in);
        } catch (Exception e) {
            throw new WeatherApiException(e);
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
