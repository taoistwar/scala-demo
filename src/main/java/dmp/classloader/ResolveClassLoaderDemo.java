package dmp.classloader;


import sun.misc.Launcher;

import java.util.Arrays;

class ResolveClassLoader extends ClassLoader {

}

class Hello {
    static {
        System.out.println("hello");
    }
}

public class ResolveClassLoaderDemo {
    public static void main(String[] args) throws Exception {
        System.out.println(Thread.currentThread().getContextClassLoader());
        ResolveClassLoader classLoader = new ResolveClassLoader();
        String name = "dmp.classloader.Hello";
        Class<?> hello = classLoader.loadClass(name); // 此种方式不会输出 “hello”
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Hello Thread CL:" + Thread.currentThread().getContextClassLoader());
            }
        });
        t.setContextClassLoader(classLoader);
        t.start();
        System.out.println(ClassLoader.getSystemClassLoader().getResource("/"));
        System.out.println(ClassLoader.getSystemClassLoader().getParent().getParent());
//        Class.forName(name, true, classLoader); // 此各方式才会输出 “hello”
    }
}
