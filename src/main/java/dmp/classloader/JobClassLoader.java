package dmp.classloader;

import dmp.classloader.loader.PathLoader;
import dmp.classloader.loader.PathLoaderFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class JobClassLoader extends ClassLoader {
    private Map<String, Class<?>> cache = new TreeMap<>();
    private List<PathLoader> loaders;

    public JobClassLoader(ClassLoader parent, String[] paths) throws IOException {
        super(parent);
        this.loaders = PathLoaderFactory.instanceMulti(paths);
    }

    /**
     * 此加载器已经加载过的类。
     *
     * @param name 类名称
     * @return 类定义
     */
    private Class<?> findLoadedClass0(String name) {
        return this.cache.get(name);
    }


    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        return this.loadClass(name, false);
    }

    @Override
    protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Class<?> clazz = null;
        // 01. 此加载器是否加载过
        clazz = this.findLoadedClass0(name);
        if (clazz != null) {
            if (resolve) {
                this.resolveClass(clazz);
            }
            return clazz;
        }
        // 02. JVM 是否加载过
        clazz = this.findLoadedClass(name);
        if (clazz != null) {
            if (resolve) {
                this.resolveClass(clazz);
            }
            return clazz;
        }
        // 03. 为了安全，强制bootstrap和ext加载器生效
        try {
            clazz = ClassLoader.getSystemClassLoader().getParent().loadClass(name);
            if (clazz != null) {
                if (resolve) {
                    this.resolveClass(clazz);
                }
                return clazz;
            }
        } catch (Exception ignored) {

        }
        // 04. 此加载器自己去加载，findClass才是当前加载器的真正加载方法。
        clazz = this.findClass(name);
        if (clazz != null) {
            if (resolve) {
                this.resolveClass(clazz);
            }
            return clazz;
        }
        // 保底
        clazz = getParent().loadClass(name);
        if (clazz != null) {
            if (resolve) {
                this.resolveClass(clazz);
            }
            return clazz;
        }
        return null;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        // 01. 获取类载体二进制数据
        byte[] data = null;
        for (PathLoader pathLoader : this.loaders) {
            try {
                data = pathLoader.readClassFile(name);
                if (data != null) {
                    break;
                }
            } catch (Exception ignored) {

            }
        }
        if (data == null) {
            throw new ClassNotFoundException(name);
        }
        // 02. 加载类
        Class<?> clazz = this.defineClass(name, data, 0, data.length);

        // 03. 添加缓存，避免重复加载
        this.cache.put(name, clazz);
        return clazz;
    }

    /**
     * 资源文件优先从Job类路径下查找
     *
     * @param resource 资源名称
     * @return 资源地址
     */
    @Override
    protected URL findResource(String resource) {
        URL url = null;
        for (PathLoader pathLoader : this.loaders) {
            try {
                url = pathLoader.getResource(resource);
                if (url != null) {
                    return url;
                }
            } catch (Exception ignored) {

            }
        }

        return super.findResource(resource);
    }


    /**
     * 资源文件优先从Job类路径下查找
     *
     * @param resource 资源文件
     * @return 输入流
     */
    @Override
    public InputStream getResourceAsStream(String resource) {
        InputStream in = null;
        for (PathLoader pathLoader : this.loaders) {
            try {
                in = pathLoader.getResourceAsStream(resource);
                if (in != null) {
                    return in;
                }
            } catch (Exception ignored) {

            }
        }
        in = super.getResourceAsStream(resource);
        if (in != null) {
            return in;
        }
        return null;
    }
}
