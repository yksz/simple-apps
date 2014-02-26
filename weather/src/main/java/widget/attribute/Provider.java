package widget.attribute;

public enum Provider {

    GOOGLE ("Google"),
    WUNDERGROUND ("Weather Underground"),
    ;

    private static final Provider[] providers = Provider.values();

    private String name;

    private Provider(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public static Provider toProvider(String name) {
        for (Provider provider : providers)
            if (provider.toString().equals(name))
                return provider;
        return null;
    }

}
