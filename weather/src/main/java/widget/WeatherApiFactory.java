package widget;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import util.Loader;
import weather.apis.WeatherApi;
import weather.apis.google.GoogleWeatherApi;
import weather.apis.wunderground.WeatherUndergroundApi;
import widget.attribute.Provider;

class WeatherApiFactory {

    private static final String WUNDERGROUND_KEY  = "wunderground.key";

    private static final WeatherApiFactory instance =  new WeatherApiFactory();
    private static String apiKey = "";

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

    public WeatherApi getWeatherAPI(Provider provider) {
        switch (provider) {
        case GOOGLE:
            if (google != null)
                return google;

            google = new GoogleWeatherApi();
            return google;

        case WUNDERGROUND:
            wunderground = new WeatherUndergroundApi(apiKey);
            return wunderground;

        default:
            throw new AssertionError("Unknown provider: " + provider);
        }
    }

    private static void loadKey() throws IOException {
        File file = Loader.getResourceAsFile(WUNDERGROUND_KEY);
        BufferedReader in = new BufferedReader(new FileReader(file));

        String line;
        try {
            while ((line = in.readLine()) != null) {
                if (line.charAt(0) == '#')
                    continue;
                apiKey = line.trim();
                break;
            }
        } finally {
            in.close();
        }
    }

}
