package weather;

import java.util.Date;

public final class Weather {

    private String condition;
    private Date date;
    private String day;
    private int lowTempF;
    private int highTempF;
    private int lowTempC;
    private int highTempC;

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getLowTempF() {
        return lowTempF;
    }

    public void setLowTempF(int lowTempF) {
        this.lowTempF = lowTempF;
    }

    public int getHighTempF() {
        return highTempF;
    }

    public void setHighTempF(int highTempF) {
        this.highTempF = highTempF;
    }

    public int getLowTempC() {
        return lowTempC;
    }

    public void setLowTempC(int lowTempC) {
        this.lowTempC = lowTempC;
    }

    public int getHighTempC() {
        return highTempC;
    }

    public void setHighTempC(int highTempC) {
        this.highTempC = highTempC;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Weather [condition=");
        builder.append(condition);
        builder.append(", date=");
        builder.append(date);
        builder.append(", day=");
        builder.append(day);
        builder.append(", lowTempF=");
        builder.append(lowTempF);
        builder.append(", highTempF=");
        builder.append(highTempF);
        builder.append(", lowTempC=");
        builder.append(lowTempC);
        builder.append(", highTempC=");
        builder.append(highTempC);
        builder.append("]");
        return builder.toString();
    }

}
