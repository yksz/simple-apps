package weather;

import java.util.Date;

public final class Weather {

    private String _condition;
    private Date _date;
    private String _day;
    private int _lowTempF;
    private int _highTempF;
    private int _lowTempC;
    private int _highTempC;

    public Date getDate() {
        return _date;
    }

    public String getCondition() {
        return _condition;
    }

    public void setCondition(String condition) {
        _condition = condition;
    }

    public void setDate(Date date) {
        _date = date;
    }

    public String getDay() {
        return _day;
    }

    public void setDay(String day) {
        _day = day;
    }

    public int getLowTempF() {
        return _lowTempF;
    }

    public void setLowTempF(int lowTempF) {
        _lowTempF = lowTempF;
    }

    public int getHighTempF() {
        return _highTempF;
    }

    public void setHighTempF(int highTempF) {
        _highTempF = highTempF;
    }

    public int getLowTempC() {
        return _lowTempC;
    }

    public void setLowTempC(int lowTempC) {
        _lowTempC = lowTempC;
    }

    public int getHighTempC() {
        return _highTempC;
    }

    public void setHighTempC(int highTempC) {
        _highTempC = highTempC;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Weather [_condition=");
        builder.append(_condition);
        builder.append(", _date=");
        builder.append(_date);
        builder.append(", _day=");
        builder.append(_day);
        builder.append(", _lowTempF=");
        builder.append(_lowTempF);
        builder.append(", _highTempF=");
        builder.append(_highTempF);
        builder.append(", _lowTempC=");
        builder.append(_lowTempC);
        builder.append(", _highTempC=");
        builder.append(_highTempC);
        builder.append("]");
        return builder.toString();
    }

}
