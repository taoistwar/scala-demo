package elt.csv;

import java.util.Collection;
import java.util.Iterator;

public class StringUtils {

    public static String join(Collection collection, String value) {
        StringBuffer builder = new StringBuffer();
        for (Iterator var3 = collection.iterator();
             var3.hasNext();
             builder.append((String) var3.next())) {
            if (builder.length() != 0) {
                builder.append(value);
            }
        }
        return builder.toString();
    }

    public static String join(Object[] arr, String value) {
        if (arr != null && arr.length > 0) {
            StringBuilder builder = new StringBuilder();
            builder.append(arr[0].toString());
            for (int i = 1; i < arr.length; i++) {
                builder.append(value);
                Object obj = arr[i];
                if (obj != null) {
                    builder.append(obj.toString());
                }
            }
            return builder.toString();
        }
        return null;
    }
}
