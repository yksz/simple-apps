package weather.api.wunderground.bean;

public class ResponseBean {

    public static class FeaturesBean {

        private int forecast;

        public int getForecast() {
            return forecast;
        }

        public void setForecast(int forecast) {
            this.forecast = forecast;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("FeaturesBean [forecast=");
            builder.append(forecast);
            builder.append("]");
            return builder.toString();
        }

    }

    private float version;
    private String termsofService;
    private FeaturesBean features;

    public float getVersion() {
        return version;
    }

    public void setVersion(float version) {
        this.version = version;
    }

    public String getTermsofService() {
        return termsofService;
    }

    public void setTermsofService(String termsofService) {
        this.termsofService = termsofService;
    }

    public FeaturesBean getFeatures() {
        return features;
    }

    public void setFeatures(FeaturesBean features) {
        this.features = features;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ResponseBean [version=");
        builder.append(version);
        builder.append(", termsofService=");
        builder.append(termsofService);
        builder.append(", features=");
        builder.append(features);
        builder.append("]");
        return builder.toString();
    }

}
