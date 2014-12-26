package weather.api;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import weather.api.google.MockGoogleWeatherAPI;
import weather.api.wunderground.MockWeatherUndergroundAPI;

public class WeatherAPITest {

    @Test
    public void testGetForecastFromGoogle() throws Exception {
        WeatherAPI api = new MockGoogleWeatherAPI();
        Forecast[] fs = api.getForecast("location");
        assertEquals("Partly Cloudy", fs[0].getCondition());
        assertEquals("Sat", fs[0].getDay());
        assertEquals(-3, fs[0].getLowTemperature().getCelsius());
        assertEquals(25, fs[0].getLowTemperature().getFahrenheit());
        assertEquals(12, fs[0].getHighTemperature().getCelsius());
        assertEquals(54, fs[0].getHighTemperature().getFahrenheit());
    }

    @Test
    public void testGetForecastFromWeatherUnderground() throws Exception {
        WeatherAPI api = new MockWeatherUndergroundAPI("key");
        Forecast[] fs = api.getForecast("location");
        assertEquals("Rain", fs[0].getCondition());
        assertEquals("Fri", fs[0].getDay());
        assertEquals(1, fs[0].getLowTemperature().getCelsius());
        assertEquals(33, fs[0].getLowTemperature().getFahrenheit());
        assertEquals(8, fs[0].getHighTemperature().getCelsius());
        assertEquals(46, fs[0].getHighTemperature().getFahrenheit());
    }

}
