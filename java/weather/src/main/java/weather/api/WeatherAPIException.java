package weather.api;

@SuppressWarnings("serial")
public class WeatherAPIException extends Exception {

    public WeatherAPIException() {
        super();
    }

    public WeatherAPIException(String message) {
        super(message);
    }

    public WeatherAPIException(String message, Throwable cause) {
        super(message, cause);
    }

    public WeatherAPIException(Throwable cause) {
        super(cause);
    }

}
