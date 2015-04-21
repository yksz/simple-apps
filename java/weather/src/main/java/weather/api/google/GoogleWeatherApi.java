package weather.api.google;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Scanner;
import java.util.regex.MatchResult;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import weather.api.Forecast;
import weather.api.Temperature;
import weather.api.WeatherApi;
import weather.api.WeatherApiException;

public class GoogleWeatherApi implements WeatherApi {

    private static final String URL = "http://www.google.com/ig/api?weather=";
    private static final String LANGUAGE = "&hl=en";

    @Override
    public Forecast[] getForecast(String location) throws WeatherApiException {
        if (location == null)
            throw new NullPointerException("location must not be null");
        if (location.isEmpty())
            throw new IllegalArgumentException("location must not be empty");

        try {
            URL url = new URL(URL + location + LANGUAGE);
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
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(in);
        Element root = doc.getDocumentElement();
        int length = root.getElementsByTagName("forecast_conditions").getLength();
        Forecast[] forecasts = new Forecast[length];

        // Today's date
        String date = doc.getDocumentElement().getElementsByTagName("forecast_date").item(0).getAttributes().item(0).getNodeValue();
        Calendar calendar = toCalendar(date);
        for (int i = 0; i < forecasts.length; i++) {
            // Weather condition
            String condition = root.getElementsByTagName("condition").item(i).getAttributes().item(0).getNodeValue();
            // Day
            String day = root.getElementsByTagName("day_of_week").item(i).getAttributes().item(0).getNodeValue();
            // Lowest temperature
            String low = root.getElementsByTagName("low").item(i).getAttributes().item(0).getNodeValue();
            int lowF = Integer.parseInt(low); // Fahrenheit
            Temperature lowTemp = Temperature.createByFahrenheit(lowF);
            // Highest temperature
            String high = root.getElementsByTagName("high").item(i).getAttributes().item(0).getNodeValue();
            int highF = Integer.parseInt(high); // Fahrenheit
            Temperature highTemp = Temperature.createByFahrenheit(highF);

            forecasts[i] = new Forecast(condition, calendar.getTime(), day, lowTemp, highTemp);
            calendar.add(Calendar.DAY_OF_MONTH, 1); // Next day
        }
        return forecasts;
    }

    /**
     * Convert String to Calendar
     *
     * @param date date(e.g. 1970-01-01)
     * @return Calendar
     */
    private Calendar toCalendar(String date) {
        Scanner scanner = new Scanner(date);
        scanner.findInLine("(\\d+)-(\\d+)-(\\d+)");
        MatchResult result = scanner.match();
        scanner.close();

        int year  = Integer.parseInt(result.group(1));
        int month = Integer.parseInt(result.group(2));
        int day   = Integer.parseInt(result.group(3));
        return new GregorianCalendar(year, month - 1, day);
    }

}
