package weather.api.wunderground;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import weather.api.Forecast;
import weather.api.Temperature;
import weather.api.WeatherAPI;
import weather.api.WeatherAPIException;
import weather.api.wunderground.bean.ForecastdayBean;
import weather.api.wunderground.bean.ForecastdayBean.DateBean;
import weather.api.wunderground.bean.ForecastdayBean.TemperatureBean;
import weather.api.wunderground.bean.WeatherBean;
import weather.api.wunderground.bean.WeatherBeanConverter;

public class WeatherUndergroundAPI implements WeatherAPI {

    private static final String URL_PREFIX = "http://api.wunderground.com/api/";
    private static final String URL_SUFFIX = "/forecast/q/";
    private static final String EXTENSION = ".json";

    private final String key;

    public WeatherUndergroundAPI(String key) {
        if (key == null)
            throw new NullPointerException("key must not be null");
        this.key = key;
    }

    @Override
    public Forecast[] getForecast(String location) throws WeatherAPIException {
        if (location == null)
            throw new NullPointerException("location must not be null");
        if (location.isEmpty())
            throw new IllegalArgumentException("location must not be empty");

        URL url;
        try {
            url = new URL(URL_PREFIX + key + URL_SUFFIX + location + EXTENSION);
        } catch (MalformedURLException e) {
            throw new WeatherAPIException(e);
        }

        HttpURLConnection http;
        try {
            http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("GET");
            http.connect();
        } catch (IOException e) {
            throw new WeatherAPIException(e);
        }

        try {
            return parse(http.getInputStream());
        } catch (Exception e) {
            throw new WeatherAPIException(e);
        } finally {
            http.disconnect();
        }
    }

    Forecast[] parse(InputStream in)
            throws JsonParseException, JsonMappingException, IOException {
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
