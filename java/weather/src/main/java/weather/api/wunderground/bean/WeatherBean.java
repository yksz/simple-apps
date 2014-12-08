package weather.api.wunderground.bean;

public class WeatherBean {

    private ResponseBean response;
    private ForecastBean forecast;

    public ResponseBean getResponse() {
        return response;
    }

    public void setResponse(ResponseBean response) {
        this.response = response;
    }

    public ForecastBean getForecast() {
        return forecast;
    }

    public void setForecast(ForecastBean forecast) {
        this.forecast = forecast;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("WeatherBean [response=");
        builder.append(response);
        builder.append(", forecast=");
        builder.append(forecast);
        builder.append("]");
        return builder.toString();
    }

}
