package weather.api.wunderground;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Objects;

import weather.api.Forecast;
import weather.api.Temperature;
import weather.api.WeatherApi;
import weather.api.WeatherApiException;
import weather.api.wunderground.bean.ForecastdayBean;
import weather.api.wunderground.bean.ForecastdayBean.DateBean;
import weather.api.wunderground.bean.ForecastdayBean.TemperatureBean;
import weather.api.wunderground.bean.WeatherBean;
import weather.api.wunderground.bean.WeatherBeanConverter;

public class WeatherUndergroundApi implements WeatherApi {

    private static final String URL_PREFIX = "http://api.wunderground.com/api/";
    private static final String URL_SUFFIX = "/forecast/q/";
    private static final String EXTENSION = ".json";

    private final String key;

    public WeatherUndergroundApi(String key) {
        this.key = Objects.requireNonNull(key, "key must not be null");
    }

    @Override
    public Forecast[] getForecast(String location) throws WeatherApiException {
        if (location == null)
            throw new NullPointerException("location must not be null");
        if (location.isEmpty())
            throw new IllegalArgumentException("location must not be empty");

        try {
            URL url = new URL(URL_PREFIX + key + URL_SUFFIX + location + EXTENSION);
            return getForecast(url);
        } catch (Exception e) {
            throw new WeatherApiException(e);
        }
    }

    private Forecast[] getForecast(URL url) throws Exception {
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setRequestMethod("GET");
        http.connect();
        try {
            return parse(http.getInputStream());
        } finally {
            http.disconnect();
        }
    }

    Forecast[] parse(InputStream in) throws Exception {
        WeatherBean weatherBean = new WeatherBeanConverter().convert(in);
        ForecastdayBean[] forecastdayBean = weatherBean.getForecast().getSimpleforecast().getForecastday();

        Forecast[] forecasts = new Forecast[forecastdayBean.length];
        for (int i = 0; i < forecasts.length; i++) {
            // Weather condition
            String condition = forecastdayBean[i].getConditions();
            // Date and Day
            DateBean date = forecastdayBean[i].getDate();
            Calendar calendar = new GregorianCalendar(
                    date.getYear(), date.getMonth() - 1, date.getDay());
            String day = date.getWeekday_short();
            // Lowest Temperature
            TemperatureBean low = forecastdayBean[i].getLow();
            Temperature lowTemp = Temperature.createByCelsius(low.getCelsius());
            // Highest Temperature
            TemperatureBean high = forecastdayBean[i].getHigh();
            Temperature highTemp = Temperature.createByCelsius(high.getCelsius());

            forecasts[i] = new Forecast(condition, calendar.getTime(), day, lowTemp, highTemp);
        }
        return forecasts;
    }

}
