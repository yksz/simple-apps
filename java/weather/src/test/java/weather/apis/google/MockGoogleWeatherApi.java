package weather.apis.google;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import util.Loader;
import weather.Weather;
import weather.WeatherException;
import weather.apis.google.GoogleWeatherApi;

public class MockGoogleWeatherApi extends GoogleWeatherApi {

    private static final String FILE_NAME = "google.xml";

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
