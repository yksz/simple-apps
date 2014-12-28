package weather.widget.config;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class Config {

    public enum Key {
        ICON_PROPERTIES ("icon.xml"),

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

    private static final String FILE_NAME = "config.xml";
    private static final Properties prop = new Properties();

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
        return prop.getProperty(key.name(), key.getDefaultValue());
    }

    public static void set(Key key, String value) {
        prop.setProperty(key.name(), value);
    }

    public static void write() throws IOException {
        OutputStream out = new FileOutputStream(FILE_NAME);
        try {
            prop.storeToXML(out, null);
        } finally {
            out.close();
        }
    }

    private static void loadProperties() throws IOException {
        InputStream in = new FileInputStream(FILE_NAME);
        try {
            prop.loadFromXML(in);
        } finally {
            in.close();
        }
    }

}
