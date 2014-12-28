package weather.widget.attribute;

public enum Provider {
    GOOGLE ("Google"),
    WUNDERGROUND ("Weather Underground"),
    ;

    private static final Provider[] providers = Provider.values();

    private final String name;

    private Provider(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public static Provider toProvider(String name) {
        for (Provider provider : providers)
            if (provider.name.equals(name))
                return provider;
        return null;
    }
}
