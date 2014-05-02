package weather.apis.wunderground.bean;

import java.util.Arrays;

public class SimpleforecastBean {

    ForecastdayBean[] forecastday;

    public ForecastdayBean[] getForecastday() {
        return forecastday;
    }

    public void setForecastday(ForecastdayBean[] forecastday) {
        this.forecastday = forecastday;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("SimpleforecastBean [forecastday=");
        builder.append(Arrays.toString(forecastday));
        builder.append("]");
        return builder.toString();
    }

}
