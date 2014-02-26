package weather.apis.wunderground;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import weather.Weather;
import weather.WeatherException;
import weather.apis.WeatherApi;
import weather.apis.wunderground.bean.ForecastdayBean;
import weather.apis.wunderground.bean.WeatherBean;
import weather.apis.wunderground.bean.WeatherBeanConverter;
import weather.apis.wunderground.bean.ForecastdayBean.DateBean;
import weather.apis.wunderground.bean.ForecastdayBean.TemperatureBean;

public class WeatherUndergroundApi implements WeatherApi {

    private static final String URL_PREFIX = "http://api.wunderground.com/api/";
    private static final String URL_SUFFIX = "/forecast/q/";
    private static final String EXTENSION = ".json";

    private final String key;

    public WeatherUndergroundApi(String key) {
        if (key == null)
            throw new NullPointerException("key must not be null");
        this.key = key;
    }

    @Override
    public Weather[] getWeather(String location) throws WeatherException {
        if (location == null)
            throw new NullPointerException("location must not be null");
        if (location.isEmpty())
            throw new IllegalArgumentException("location must not be empty");

        URL url;
        try {
            url = new URL(URL_PREFIX + key + URL_SUFFIX + location + EXTENSION);
        } catch (MalformedURLException e) {
            throw new WeatherException(e);
        }

        HttpURLConnection http;
        try {
            http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("GET");
            http.connect();
        } catch (IOException e) {
            throw new WeatherException(e);
        }

        Weather[] weather;
        try {
            weather = this.parse(http.getInputStream());
        } catch (Exception e) {
            throw new WeatherException(e);
        } finally {
            http.disconnect();
        }

        return weather;
    }

    Weather[] parse(InputStream in)
            throws JsonParseException, JsonMappingException, IOException {
        WeatherBean weatherBean = new WeatherBeanConverter().convert(in);
        ForecastdayBean[] forecastdayBean = weatherBean.getForecast().getSimpleforecast().getForecastday();

        Weather[] result = new Weather[forecastdayBean.length];
        for (int i = 0; i < result.length; i++) {
            Weather weather = new Weather();

            // Weather condition
            String condition = forecastdayBean[i].getConditions();
            weather.setCondition(condition);

            // Date and Day
            DateBean date = forecastdayBean[i].getDate();
            Calendar calendar = new GregorianCalendar(
                    date.getYear(), date.getMonth() - 1, date.getDay());
            weather.setDate(calendar.getTime());
            weather.setDay(date.getWeekday_short());

            // Lowest Temperature
            TemperatureBean low = forecastdayBean[i].getLow();
            weather.setLowTempF(low.getFahrenheit());
            weather.setLowTempC(low.getCelsius());

            // Highest Temperature
            TemperatureBean high = forecastdayBean[i].getHigh();
            weather.setHighTempF(high.getFahrenheit());
            weather.setHighTempC(high.getCelsius());

            result[i] = weather;
        }

        return result;
    }

}
