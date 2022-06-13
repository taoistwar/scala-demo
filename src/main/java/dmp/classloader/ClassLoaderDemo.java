package dmp.classloader;

import dmp.compute.flink.PreviewFlinkApp;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ClassLoaderDemo {


    public static void main(String[] args) throws Exception {
        PreviewFlinkApp p = new PreviewFlinkApp();
        System.out.println(p);

        run();
    }

    public static void run() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        String[] paths = new String[]{"D:\\Workspaces\\java\\haima-dmp-compute\\runtime-flink\\target\\runtime-flink-1.0.1-preview.jar"};
        JobClassLoader cl = new JobClassLoader(ClassLoader.getSystemClassLoader(), paths);

        Class<?> app = cl.loadClass("dmp.compute.flink.RuntimeFlinkApp");
        Object args = new String[]{"1"};
        Method main = app.getMethod("main", args.getClass());
        main.invoke(app, args);
    }
}
