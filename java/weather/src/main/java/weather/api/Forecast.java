package weather.api;

import java.util.Date;

public final class Forecast {

    private final String condition;
    private final Date date;
    private final String day;
    private final Temperature lowTemperature;
    private final Temperature highTemperature;

    public Forecast(String condition, Date date, String day,
            Temperature lowTemperature, Temperature highTemperature) {
        this.condition = condition;
        this.date = date;
        this.day = day;
        this.lowTemperature = lowTemperature;
        this.highTemperature = highTemperature;
    }

    public String getCondition() {
        return condition;
    }

    public Date getDate() {
        return date;
    }

    public String getDay() {
        return day;
    }

    public Temperature getLowTemperature() {
        return lowTemperature;
    }

    public Temperature getHighTemperature() {
        return highTemperature;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Forecast [condition=");
        builder.append(condition);
        builder.append(", date=");
        builder.append(date);
        builder.append(", day=");
        builder.append(day);
        builder.append(", lowTemperature=");
        builder.append(lowTemperature);
        builder.append(", highTemperature=");
        builder.append(highTemperature);
        builder.append("]");
        return builder.toString();
    }

}
