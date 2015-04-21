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
        POSITION_X ("100"),
        POSITION_Y ("100"),
        PROVIDER ("Weather Underground"),
        LOCATION (""),
        ;

        private final String defaultValue;

        private Key(String defaultValue) {
            this.defaultValue = defaultValue;
        }

        public String getDefaultValue() {
            return defaultValue;
        }
    }

    private static final String CONFIG_FILE = "config.xml";
    private static final Properties props = new Properties();

    static {
        try {
            loadProperties();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Config() {
    }

    public static String get(Key key) {
        return props.getProperty(key.name(), key.getDefaultValue());
    }

    public static void set(Key key, String value) {
        props.setProperty(key.name(), value);
    }

    public static void write() throws IOException {
        try (OutputStream out = new FileOutputStream(CONFIG_FILE)) {
            props.storeToXML(out, null);
        }
    }

    private static void loadProperties() throws IOException {
        InputStream in = new FileInputStream(CONFIG_FILE);
        props.loadFromXML(in);
    }

}
