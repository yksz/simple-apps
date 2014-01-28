package widget.config;

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

        private String _defaultValue;

        private Key(String defaultValue) {
            _defaultValue = defaultValue;
        }

        public String getDefaultValue() {
            return _defaultValue;
        }

    }

    private static final String FILE_NAME = "config.xml";
    private static final Properties __prop = new Properties();

    static {
        try {
            loadProperties(FILE_NAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Config() {
    }

    public static String get(Key key) {
        return __prop.getProperty(key.name(), key.getDefaultValue());
    }

    public static void set(Key key, String value) {
        __prop.setProperty(key.name(), value);
    }

    public static void write() throws IOException {
        OutputStream out = new FileOutputStream(FILE_NAME);
        try {
            __prop.storeToXML(out, null);
        } finally {
            out.close();
        }
    }

    private static void loadProperties(String filename) throws IOException {
        InputStream in = new FileInputStream(FILE_NAME);
        try {
            __prop.loadFromXML(in);
        } finally {
            in.close();
        }
    }

}
