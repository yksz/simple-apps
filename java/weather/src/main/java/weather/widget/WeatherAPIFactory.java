package weather.widget;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import weather.api.WeatherAPI;
import weather.api.google.GoogleWeatherAPI;
import weather.api.wunderground.WeatherUndergroundAPI;
import weather.widget.attribute.Provider;
import weather.widget.util.Loader;

class WeatherAPIFactory {

    private static final String WUNDERGROUND_KEY  = "wunderground.key";

    private static final WeatherAPIFactory instance =  new WeatherAPIFactory();
    private static String apiKey = "";

    private WeatherAPI google, wunderground;

    static {
        try {
            loadKey();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private WeatherAPIFactory() {
    }

    public static WeatherAPIFactory getInstance() {
        return instance;
    }

    public WeatherAPI getWeatherAPI(Provider provider) {
        switch (provider) {
        case GOOGLE:
            if (google != null)
                return google;

            google = new GoogleWeatherAPI();
            return google;

        case WUNDERGROUND:
            wunderground = new WeatherUndergroundAPI(apiKey);
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
