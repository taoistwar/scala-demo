package dmp.classloader.loader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PathLoaderFactory {
    public static List<PathLoader> instanceMulti(String[] paths) throws IOException {
        List<PathLoader> list = new ArrayList<>();
        for (String path : paths) {
            PathLoader loader = PathLoaderFactory.instance(path);
            if (loader != null) {
                list.add(loader);
            }
        }
        return list;
    }

    public static PathLoader instance(String path) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            return null;
        }
        if (file.getName().toLowerCase().endsWith(".jar")) {
            return new JarPathLoader(file);
        }
        if (file.getName().endsWith("*")) {
            return new WildcardPathLoader(file);
        }
        return new FilePathLoader(file);
    }
}
