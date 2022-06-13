package dmp.classloader.loader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class JarPathLoader implements PathLoader {
    private final File file;
    private final ZipFile zipFile;

    public JarPathLoader(File file) throws IOException {
        this.file = file;
        this.zipFile = new ZipFile(this.file);
    }


    @Override
    public byte[] readClassFile(String name) throws IOException {
        String path = name.replace(".", "/") + ".class";
        ZipEntry zipEntry = zipFile.getEntry(path);
        return read(zipFile, zipEntry);
    }

    @Override
    public URL getResource(String name) throws IOException {
        ZipEntry zipEntry = zipFile.getEntry(name);
        if (zipEntry != null) {
            String url = "file://" + this.file.getAbsolutePath() + "!" + name;
            System.out.println(url);
            return new URL(url);
        }
        return null;
    }

    @Override
    public InputStream getResourceAsStream(String name) throws IOException {
        ZipEntry zipEntry = zipFile.getEntry(name);
        if (zipEntry != null) {
            return zipFile.getInputStream(zipEntry);
        }
        return null;
    }

    @Override
    public void close() {
        try {
            this.zipFile.close();
        } catch (IOException ignored) {
        }
    }

    private byte[] read(ZipFile zipFile, ZipEntry zipEntry) throws IOException {
        InputStream in = zipFile.getInputStream(zipEntry);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] arr = new byte[1024];
        int count = 0;
        while ((count = in.read(arr)) != -1) {
            out.write(arr, 0, count);
        }
        return out.toByteArray();
    }
}
