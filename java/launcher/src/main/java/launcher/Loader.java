package launcher;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

class Loader {

    public static URL getResource(String resource) {
        if (resource == null)
            throw new NullPointerException("resource must not be null");
        if (resource.isEmpty())
            throw new IllegalArgumentException("resource must not be empty");

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (resource.charAt(0) == '/')
            return classLoader.getResource(resource.substring(1));
        else
            return classLoader.getResource(resource);
    }

    public static InputStream getResourceAsStream(String resource) throws IOException {
        URL url = getResource(resource);
        if (url == null)
            throw new IOException("Could not find the resource: " + resource);
        else
            return url.openStream();
    }

}
