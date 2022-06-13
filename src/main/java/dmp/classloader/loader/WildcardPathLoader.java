package dmp.classloader.loader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class WildcardPathLoader implements PathLoader {
    private final File file;
    public WildcardPathLoader(File file) {
        this.file = file;
    }
    @Override
    public byte[] readClassFile(String name) {
        return null;
    }

    @Override
    public URL getResource(String name) {
        return null;
    }

    @Override
    public InputStream getResourceAsStream(String name) throws IOException {
        return null;
    }

    @Override
    public void close() {

    }
}
