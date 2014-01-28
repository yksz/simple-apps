package widget.attribute;

public enum Provider {

    GOOGLE ("Google"),
    WUNDERGROUND ("Weather Underground"),
    ;

    private static final Provider[] __providers = Provider.values();

    private String _name;

    private Provider(String name) {
        _name = name;
    }

    @Override
    public String toString() {
        return _name;
    }

    public static Provider toProvider(String name) {
        for (Provider provider : __providers)
            if (provider.toString().equals(name))
                return provider;
        return null;
    }

}
