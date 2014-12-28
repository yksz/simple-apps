package weather.widget;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import weather.api.WeatherApi;
import weather.api.google.GoogleWeatherApi;
import weather.api.wunderground.WeatherUndergroundApi;
import weather.widget.attribute.Provider;
import weather.widget.util.Loader;

class WeatherApiFactory {

    private static final String WUNDERGROUND_KEY_FILE  = "wunderground.key";
    private static final WeatherApiFactory instance =  new WeatherApiFactory();
    private static String wundergroundApiKey = "";

    private WeatherApi google, wunderground;

    static {
        try {
            loadKey();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private WeatherApiFactory() {
    }

    public static WeatherApiFactory getInstance() {
        return instance;
    }

    public WeatherApi getWeatherApi(Provider provider) {
        switch (provider) {
        case GOOGLE:
            if (google != null)
                return google;
            return google = new GoogleWeatherApi();

        case WUNDERGROUND:
            if (wunderground != null)
                return wunderground;
            return wunderground = new WeatherUndergroundApi(wundergroundApiKey);

        default:
            throw new AssertionError("Unknown provider: " + provider);
        }
    }

    private static void loadKey() throws IOException {
        File file = Loader.getResourceAsFile(WUNDERGROUND_KEY_FILE);
        BufferedReader in = new BufferedReader(new FileReader(file));
        try {
            String line;
            while ((line = in.readLine()) != null) {
                if (line.charAt(0) == '#')
                    continue;
                wundergroundApiKey = line.trim();
                break;
            }
        } finally {
            in.close();
        }
    }

}
