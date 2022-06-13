package elt.csv;

import java.util.List;

public class BaseValidate {
    public static boolean notEmpty(List<?> list) {
        return list != null && list.size() > 0;
    }

    public static boolean notEmpty(Object[] list) {
        return list != null && list.length > 0;
    }

    public static boolean isEmpty(Object[] list) {
        return list == null || list.length == 0;
    }

    public static boolean isEmpty(List<?> list) {
        return list == null || list.size() == 0;
    }

    public static boolean isEmpty(Object msg) {
        return msg == null || "".equals(msg);
    }

    public static boolean isEmpty(String msg) {
        return msg == null || msg.length() == 0;
    }

    public static boolean notEmpty(String msg) {
        return msg != null && msg.length() > 0;
    }
}
