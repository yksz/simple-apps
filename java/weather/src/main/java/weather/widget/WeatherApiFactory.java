package weather.widget;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import weather.api.WeatherApi;
import weather.api.google.GoogleWeatherApi;
import weather.api.wunderground.WeatherUndergroundApi;

class WeatherApiFactory {

    private static final String WUNDERGROUND_KEY_FILE = "wunderground.key";
    private static String wundergroundApiKey = "";
    private static WeatherApi google, wunderground;

    static {
        try {
            loadKey();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private WeatherApiFactory() {
    }

    public static WeatherApi getWeatherApi(Provider provider) {
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
        try (InputStream in = Loader.getResourceAsStream(WUNDERGROUND_KEY_FILE);
                BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.charAt(0) == '#')
                    continue;
                wundergroundApiKey = line.trim();
                break;
            }
        }
    }

}
