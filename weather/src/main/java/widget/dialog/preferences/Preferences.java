package widget.dialog.preferences;

import widget.attribute.Provider;

public class Preferences {

    private Provider _provider;
    private String _location;

    public Provider getProvider() {
        return _provider;
    }

    public void setProvider(Provider provider) {
        _provider = provider;
    }

    public String getLocation() {
        return _location;
    }

    public void setLocation(String location) {
        _location = location;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Preferences [_provider=");
        builder.append(_provider);
        builder.append(", _location=");
        builder.append(_location);
        builder.append("]");
        return builder.toString();
    }

}
