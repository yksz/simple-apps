package weather.api.google;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import weather.api.Forecast;
import weather.api.WeatherAPIException;
import weather.widget.util.Loader;

public class MockGoogleWeatherAPI extends GoogleWeatherAPI {

    private static final String FILE_NAME = "google.xml";

    @Override
    public Forecast[] getForecast(String location) throws WeatherAPIException {
        InputStream in;
        try {
            File file = Loader.getResourceAsFile(FILE_NAME);
            in = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new WeatherAPIException(e);
        }

        try {
            return super.parse(in);
        } catch (Exception e) {
            throw new WeatherAPIException(e);
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
