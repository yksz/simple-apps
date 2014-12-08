package weather.api;

public class Temperature {

    private final int celsius;
    private final int fahrenheit;

    public static Temperature createByCelsius(int c) {
        int f = (int) ((9 / 5.0) * c + 32);
        return new Temperature(c, f);
    }

    public static Temperature createByFahrenheit(int f) {
        int c = (int) ((5 / 9.0) * (f - 32));
        return new Temperature(c, f);
    }

    public Temperature(int celsius, int fahrenheit) {
        this.celsius = celsius;
        this.fahrenheit = fahrenheit;
    }

    public int getCelsius() {
        return celsius;
    }

    public int getFahrenheit() {
        return fahrenheit;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Temperature [celsius=");
        builder.append(celsius);
        builder.append(", fahrenheit=");
        builder.append(fahrenheit);
        builder.append("]");
        return builder.toString();
    }

}
