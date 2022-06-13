package demo.jna;

public class IntDemo {
    public static void main(String[] args) throws InstantiationException, IllegalAccessException {
        // 基础类型无法Class<int>
        Class<?> clazz = int.class;
        System.out.println(clazz.getName());
        // 可以使用封装类型Class<Integer>编译通过,但还是基础类型.
        Class<Integer> clazz2 = int.class;
        System.out.println(clazz2);
        Class<Integer> clazz3 = Integer.class;
        System.out.println(clazz3);
    }
}
