package weather.apis.wunderground;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import util.Loader;
import weather.Weather;
import weather.WeatherException;
import weather.apis.wunderground.WeatherUndergroundApi;

public class MockWeatherUndergroundApi extends WeatherUndergroundApi {

    private static final String FILE_NAME = "wunderground.json";

    public MockWeatherUndergroundApi(String key) {
        super(key);
    }

    @Override
    public Weather[] getWeather(String location) throws WeatherException {
        InputStream in;
        try {
            File file = Loader.getResourceAsFile(FILE_NAME);
            in = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new WeatherException(e);
        }

        try {
            return super.parse(in);
        } catch (Exception e) {
            throw new WeatherException(e);
        }
    }

}
