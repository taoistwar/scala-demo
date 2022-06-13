package dmp.proxy;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;

public class DynamicProxyTest {

    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Person renter = new Renter();
        Person proxy1 = demo1(renter);
        proxy1.rentHouse();
        Person proxy2 = demo2(renter);
        proxy2.rentHouse();
    }

    public static Person demo2(Person renter) {
        //创建InvocationHandler对象
        InvocationHandler renterHandler = new RenterInvocationHandler<>(renter);

        //创建代理对象,代理对象的每个执行方法都会替换执行Invocation中的invoke方法
        return (Person) Proxy.newProxyInstance(Person.class.getClassLoader(), new Class<?>[]{Person.class}, renterHandler);
    }

    public static Person demo1(Person renter) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        //创建InvocationHandler对象
        InvocationHandler renterHandler = new RenterInvocationHandler<>(renter);

        //使用Proxy类的getProxyClass静态方法生成一个动态代理类renterProxy
        Class<?> renterProxyClass = Proxy.getProxyClass(Person.class.getClassLoader(), Person.class);
        //获取代理类renterProxy的构造器，参数为InvocationHandler
        Constructor<?> constructor = renterProxyClass.getConstructor(InvocationHandler.class);
        //使用构造器创建一个代理类实例对象
        return (Person) constructor.newInstance(renterHandler);
    }
}
