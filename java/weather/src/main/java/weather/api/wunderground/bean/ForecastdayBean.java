package weather.api.wunderground.bean;

public class ForecastdayBean {

    public static class DateBean {

        private long epoch;
        private String pretty;
        private int day;
        private int month;
        private int year;
        private int yday;
        private int hour;
        private int min;
        private int sec;
        private int isdst;
        private String monthname;
        private String monthname_short;
        private String weekday_short;
        private String weekday;
        private String ampm;
        private String tz_short;
        private String tz_long;

        public long getEpoch() {
            return epoch;
        }

        public void setEpoch(long epoch) {
            this.epoch = epoch;
        }

        public String getPretty() {
            return pretty;
        }

        public void setPretty(String pretty) {
            this.pretty = pretty;
        }

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
        }

        public int getMonth() {
            return month;
        }

        public void setMonth(int month) {
            this.month = month;
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public int getYday() {
            return yday;
        }

        public void setYday(int yday) {
            this.yday = yday;
        }

        public int getHour() {
            return hour;
        }

        public void setHour(int hour) {
            this.hour = hour;
        }

        public int getMin() {
            return min;
        }

        public void setMin(int min) {
            this.min = min;
        }

        public int getSec() {
            return sec;
        }

        public void setSec(int sec) {
            this.sec = sec;
        }

        public int getIsdst() {
            return isdst;
        }

        public void setIsdst(int isdst) {
            this.isdst = isdst;
        }

        public String getMonthname() {
            return monthname;
        }

        public void setMonthname(String monthname) {
            this.monthname = monthname;
        }

        public String getMonthname_short() {
            return monthname_short;
        }

        public void setMonthname_short(String monthname_short) {
            this.monthname_short = monthname_short;
        }

        public String getWeekday_short() {
            return weekday_short;
        }

        public void setWeekday_short(String weekday_short) {
            this.weekday_short = weekday_short;
        }

        public String getWeekday() {
            return weekday;
        }

        public void setWeekday(String weekday) {
            this.weekday = weekday;
        }

        public String getAmpm() {
            return ampm;
        }

        public void setAmpm(String ampm) {
            this.ampm = ampm;
        }

        public String getTz_short() {
            return tz_short;
        }

        public void setTz_short(String tz_short) {
            this.tz_short = tz_short;
        }

        public String getTz_long() {
            return tz_long;
        }

        public void setTz_long(String tz_long) {
            this.tz_long = tz_long;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("DateBean [epoch=");
            builder.append(epoch);
            builder.append(", pretty=");
            builder.append(pretty);
            builder.append(", day=");
            builder.append(day);
            builder.append(", month=");
            builder.append(month);
            builder.append(", year=");
            builder.append(year);
            builder.append(", yday=");
            builder.append(yday);
            builder.append(", hour=");
            builder.append(hour);
            builder.append(", min=");
            builder.append(min);
            builder.append(", sec=");
            builder.append(sec);
            builder.append(", isdst=");
            builder.append(isdst);
            builder.append(", monthname=");
            builder.append(monthname);
            builder.append(", monthname_short=");
            builder.append(monthname_short);
            builder.append(", weekday_short=");
            builder.append(weekday_short);
            builder.append(", weekday=");
            builder.append(weekday);
            builder.append(", ampm=");
            builder.append(ampm);
            builder.append(", tz_short=");
            builder.append(tz_short);
            builder.append(", tz_long=");
            builder.append(tz_long);
            builder.append("]");
            return builder.toString();
        }

    }

    public static class TemperatureBean {

        private int fahrenheit;
        private int celsius;

        public int getFahrenheit() {
            return fahrenheit;
        }

        public void setFahrenheit(int fahrenheit) {
            this.fahrenheit = fahrenheit;
        }

        public int getCelsius() {
            return celsius;
        }

        public void setCelsius(int celsius) {
            this.celsius = celsius;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("TemperatureBean [fahrenheit=");
            builder.append(fahrenheit);
            builder.append(", celsius=");
            builder.append(celsius);
            builder.append("]");
            return builder.toString();
        }

    }

    public static class QpfBean {

        private float in;
        private float mm;

        public float getIn() {
            return in;
        }

        public void setIn(float in) {
            this.in = in;
        }

        public float getMm() {
            return mm;
        }

        public void setMm(float mm) {
            this.mm = mm;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("QpfBean [in=");
            builder.append(in);
            builder.append(", mm=");
            builder.append(mm);
            builder.append("]");
            return builder.toString();
        }

    }

    public static class SnowBean {

        private float in;
        private float cm;

        public float getIn() {
            return in;
        }

        public void setIn(float in) {
            this.in = in;
        }

        public float getCm() {
            return cm;
        }

        public void setCm(float cm) {
            this.cm = cm;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("SnowBean [in=");
            builder.append(in);
            builder.append(", cm=");
            builder.append(cm);
            builder.append("]");
            return builder.toString();
        }

    }

    public static class WindBean {

        private int mph;
        private int kph;
        private String dir;
        private int degrees;

        public int getMph() {
            return mph;
        }

        public void setMph(int mph) {
            this.mph = mph;
        }

        public int getKph() {
            return kph;
        }

        public void setKph(int kph) {
            this.kph = kph;
        }

        public String getDir() {
            return dir;
        }

        public void setDir(String dir) {
            this.dir = dir;
        }

        public int getDegrees() {
            return degrees;
        }

        public void setDegrees(int degrees) {
            this.degrees = degrees;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("WindBean [mph=");
            builder.append(mph);
            builder.append(", kph=");
            builder.append(kph);
            builder.append(", dir=");
            builder.append(dir);
            builder.append(", degrees=");
            builder.append(degrees);
            builder.append("]");
            return builder.toString();
        }

    }

    private DateBean date;
    private int period;
    private TemperatureBean high;
    private TemperatureBean low;
    private String conditions;
    private String icon;
    private String icon_url;
    private String skyicon;
    private int pop;
    private QpfBean qpf_allday;
    private QpfBean qpf_day;
    private QpfBean qpf_night;
    private SnowBean snow_allday;
    private SnowBean snow_day;
    private SnowBean snow_night;
    private WindBean maxwind;
    private WindBean avewind;
    private int avehumidity;
    private int maxhumidity;
    private int minhumidity;

    public DateBean getDate() {
        return date;
    }

    public void setDate(DateBean date) {
        this.date = date;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public TemperatureBean getHigh() {
        return high;
    }

    public void setHigh(TemperatureBean high) {
        this.high = high;
    }

    public TemperatureBean getLow() {
        return low;
    }

    public void setLow(TemperatureBean low) {
        this.low = low;
    }

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIcon_url() {
        return icon_url;
    }

    public void setIcon_url(String icon_url) {
        this.icon_url = icon_url;
    }

    public String getSkyicon() {
        return skyicon;
    }

    public void setSkyicon(String skyicon) {
        this.skyicon = skyicon;
    }

    public int getPop() {
        return pop;
    }

    public void setPop(int pop) {
        this.pop = pop;
    }

    public QpfBean getQpf_allday() {
        return qpf_allday;
    }

    public void setQpf_allday(QpfBean qpf_allday) {
        this.qpf_allday = qpf_allday;
    }

    public QpfBean getQpf_day() {
        return qpf_day;
    }

    public void setQpf_day(QpfBean qpf_day) {
        this.qpf_day = qpf_day;
    }

    public QpfBean getQpf_night() {
        return qpf_night;
    }

    public void setQpf_night(QpfBean qpf_night) {
        this.qpf_night = qpf_night;
    }

    public SnowBean getSnow_allday() {
        return snow_allday;
    }

    public void setSnow_allday(SnowBean snow_allday) {
        this.snow_allday = snow_allday;
    }

    public SnowBean getSnow_day() {
        return snow_day;
    }

    public void setSnow_day(SnowBean snow_day) {
        this.snow_day = snow_day;
    }

    public SnowBean getSnow_night() {
        return snow_night;
    }

    public void setSnow_night(SnowBean snow_night) {
        this.snow_night = snow_night;
    }

    public WindBean getMaxwind() {
        return maxwind;
    }

    public void setMaxwind(WindBean maxwind) {
        this.maxwind = maxwind;
    }

    public WindBean getAvewind() {
        return avewind;
    }

    public void setAvewind(WindBean avewind) {
        this.avewind = avewind;
    }

    public int getAvehumidity() {
        return avehumidity;
    }

    public void setAvehumidity(int avehumidity) {
        this.avehumidity = avehumidity;
    }

    public int getMaxhumidity() {
        return maxhumidity;
    }

    public void setMaxhumidity(int maxhumidity) {
        this.maxhumidity = maxhumidity;
    }

    public int getMinhumidity() {
        return minhumidity;
    }

    public void setMinhumidity(int minhumidity) {
        this.minhumidity = minhumidity;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ForecastdayBean [date=");
        builder.append(date);
        builder.append(", period=");
        builder.append(period);
        builder.append(", high=");
        builder.append(high);
        builder.append(", low=");
        builder.append(low);
        builder.append(", conditions=");
        builder.append(conditions);
        builder.append(", icon=");
        builder.append(icon);
        builder.append(", icon_url=");
        builder.append(icon_url);
        builder.append(", skyicon=");
        builder.append(skyicon);
        builder.append(", pop=");
        builder.append(pop);
        builder.append(", qpf_allday=");
        builder.append(qpf_allday);
        builder.append(", qpf_day=");
        builder.append(qpf_day);
        builder.append(", qpf_night=");
        builder.append(qpf_night);
        builder.append(", snow_allday=");
        builder.append(snow_allday);
        builder.append(", snow_day=");
        builder.append(snow_day);
        builder.append(", snow_night=");
        builder.append(snow_night);
        builder.append(", maxwind=");
        builder.append(maxwind);
        builder.append(", avewind=");
        builder.append(avewind);
        builder.append(", avehumidity=");
        builder.append(avehumidity);
        builder.append(", maxhumidity=");
        builder.append(maxhumidity);
        builder.append(", minhumidity=");
        builder.append(minhumidity);
        builder.append("]");
        return builder.toString();
    }

}
