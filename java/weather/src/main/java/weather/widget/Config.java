package weather.widget;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

class Config {

    public enum Key {
        ICON_PROPERTIES_FILE ("icon.xml"),
        WIDGET_X ("100"),
        WIDGET_Y ("100"),
        WEBAPI_PROVIDER ("Weather Underground"),
        LOCATION (""),
        ;

        private final String name;
        private final String defaultValue;

        private Key(String defaultValue) {
            name = name().toLowerCase().replaceAll("_", ".");
            this.defaultValue = defaultValue;
        }

        @Override
        public String toString() {
            return name;
        }

        public String getDefaultValue() {
            return defaultValue;
        }
    }

    private static final String CONFIG_FILE = "config.xml";
    private static final Properties props = new Properties();

    static {
        try {
            load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Config() {
    }

    public static String get(Key key) {
        return props.getProperty(key.toString(), key.getDefaultValue());
    }

    public static void set(Key key, String value) {
        props.setProperty(key.toString(), value);
    }

    public static void store() throws IOException {
        try (OutputStream out = new FileOutputStream(CONFIG_FILE)) {
            props.storeToXML(out, null);
        }
    }

    private static void load() throws IOException {
        InputStream in = new FileInputStream(CONFIG_FILE);
        props.loadFromXML(in);
    }

}
