package weather.apis.wunderground.bean;

import java.util.Arrays;

public class TxtForecastBean {

    private String date;
    private TxtForecastdayBean[] forecastday;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public TxtForecastdayBean[] getForecastday() {
        return forecastday;
    }

    public void setForecastday(TxtForecastdayBean[] forecastday) {
        this.forecastday = forecastday;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("TxtForecastBean [date=");
        builder.append(date);
        builder.append(", forecastday=");
        builder.append(Arrays.toString(forecastday));
        builder.append("]");
        return builder.toString();
    }

}
