package weather;

public class WeatherException extends Exception {

    private static final long serialVersionUID = -4647119957280049907L;

    public WeatherException() {
        super();
    }

    public WeatherException(String message) {
        super(message);
    }

    public WeatherException(String message, Throwable cause) {
        super(message, cause);
    }

    public WeatherException(Throwable cause) {
        super(cause);
    }

}
