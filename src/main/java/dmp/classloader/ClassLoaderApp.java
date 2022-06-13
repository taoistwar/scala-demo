package dmp.classloader;

public class ClassLoaderApp {
    public static void main(String[] args) {
        ClassLoader bootstrapClassLoader = String.class.getClassLoader();
        ClassLoader appClassLoader = ClassLoaderApp.class.getClassLoader();
        ClassLoader extClassLoader = appClassLoader.getParent();
        // openjdk中是：null（JVM内置的，JAVA程序无法访问）
        System.out.println(bootstrapClassLoader);
        // openjdk中是：sun.misc.Launcher$AppClassLoader
        System.out.println(appClassLoader.getClass().getName());
        // openjdk中是： sun.misc.Launcher$ExtClassLoader
        System.out.println(extClassLoader.getClass().getName());
    }
}
