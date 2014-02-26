package weather.apis.google;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Scanner;
import java.util.regex.MatchResult;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import weather.Weather;
import weather.WeatherException;
import weather.apis.WeatherApi;

public class GoogleWeatherApi implements WeatherApi {

    private static final String URL = "http://www.google.com/ig/api?weather=";
    private static final String LANGUAGE = "&hl=en";

    @Override
    public Weather[] getWeather(String location) throws WeatherException {
        if (location == null)
            throw new NullPointerException("location must not be null");
        if (location.isEmpty())
            throw new IllegalArgumentException("location must not be empty");

        URL url;
        try {
            url = new URL(URL + location + LANGUAGE);
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
            weather = parse(http.getInputStream());
        } catch (Exception e) {
            throw new WeatherException(e);
        } finally {
            http.disconnect();
        }

        return weather;
    }

    Weather[] parse(InputStream in)
            throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(in);

        Element root = doc.getDocumentElement();
        int length = root.getElementsByTagName("forecast_conditions").getLength();
        Weather[] result = new Weather[length];

        // Today's date
        String date = doc.getDocumentElement().getElementsByTagName("forecast_date").item(0).getAttributes().item(0).getNodeValue();
        Calendar calendar = toCalendar(date);

        for (int i = 0; i < result.length; i++) {
            Weather weather = new Weather();

            // Weather condition
            String condition = root.getElementsByTagName("condition").item(i).getAttributes().item(0).getNodeValue();
            weather.setCondition(condition);

            // Date
            weather.setDate(calendar.getTime());

            // Day
            String day = root.getElementsByTagName("day_of_week").item(i).getAttributes().item(0).getNodeValue();
            weather.setDay(day);

            // Lowest temperature
            String low = root.getElementsByTagName("low").item(i).getAttributes().item(0).getNodeValue();
            int lowF = Integer.parseInt(low);           // Fahrenheit
            int lowC = (int) ((5 / 9.0) * (lowF - 32)); // Celsius
            weather.setLowTempF(lowF);
            weather.setLowTempC(lowC);

            // Highest temperature
            String high = root.getElementsByTagName("high").item(i).getAttributes().item(0).getNodeValue();
            int highF = Integer.parseInt(high);           // Fahrenheit
            int highC = (int) ((5 / 9.0) * (highF - 32)); // Celsius
            weather.setHighTempF(highF);
            weather.setHighTempC(highC);

            result[i] = weather;

            calendar.add(Calendar.DAY_OF_MONTH, 1); // Next day
        }

        return result;
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

        int year  = Integer.parseInt(result.group(1));
        int month = Integer.parseInt(result.group(2));
        int day   = Integer.parseInt(result.group(3));

        return new GregorianCalendar(year, month - 1, day);
    }

}
