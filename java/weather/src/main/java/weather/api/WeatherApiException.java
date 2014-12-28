package weather.api;

@SuppressWarnings("serial")
public class WeatherApiException extends Exception {

    public WeatherApiException() {
        super();
    }

    public WeatherApiException(String message) {
        super(message);
    }

    public WeatherApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public WeatherApiException(Throwable cause) {
        super(cause);
    }

}
