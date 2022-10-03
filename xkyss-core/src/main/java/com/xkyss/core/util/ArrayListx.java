package com.xkyss.core.util;
import java.util.*;

public class ArrayListx extends Listx {

    @SafeVarargs
    public static <T> ArrayList<T> of(T... values) {
        if (Arrayx.isEmpty(values)) {
            return new ArrayList<>();
        }

        ArrayList<T> list = new ArrayList<>(values.length);
        Collections.addAll(list, values);
        return list;
    }

    public static <T> ArrayList<T> of(Collection<T> collection) {
        if (isNullOrEmpty(collection)) {
            return new ArrayList<>();
        }

        return new ArrayList<>(collection);
    }

    public static <T> ArrayList<T> of(Iterable<T> iterable) {
        if (null == iterable) {
            return new ArrayList<>();
        }

        return of(iterable.iterator());
    }

    public static <T> ArrayList<T> of(Iterator<T> iterator) {
        ArrayList<T> list = new ArrayList<>();
        if (null != iterator) {
            while (iterator.hasNext()) {
                list.add(iterator.next());
            }
        }
        return list;
    }

    public static <T> ArrayList<T> of(Enumeration<T> enumeration) {
        ArrayList<T> list = new ArrayList<>();
        if (null != enumeration) {
            while (enumeration.hasMoreElements()) {
                list.add(enumeration.nextElement());
            }
        }
        return list;
    }
}
