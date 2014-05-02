package weather.apis.wunderground.bean;

public class ForecastBean {

    private TxtForecastBean txt_forecast;
    private SimpleforecastBean simpleforecast;

    public TxtForecastBean getTxt_forecast() {
        return txt_forecast;
    }

    public void setTxt_forecast(TxtForecastBean txt_forecast) {
        this.txt_forecast = txt_forecast;
    }

    public SimpleforecastBean getSimpleforecast() {
        return simpleforecast;
    }

    public void setSimpleforecast(SimpleforecastBean simpleforecast) {
        this.simpleforecast = simpleforecast;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ForecastBean [txt_forecast=");
        builder.append(txt_forecast);
        builder.append(", simpleforecast=");
        builder.append(simpleforecast);
        builder.append("]");
        return builder.toString();
    }

}
