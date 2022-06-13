package dmp.classloader.loader;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public interface PathLoader {
    byte[] readClassFile(String name) throws IOException;

    URL getResource(String name) throws IOException;

    InputStream getResourceAsStream(String name) throws IOException;

    void close();
}
