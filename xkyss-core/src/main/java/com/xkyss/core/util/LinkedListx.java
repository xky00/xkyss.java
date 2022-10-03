package com.xkyss.core.util;

import java.util.*;

public class LinkedListx extends Listx {

    @SafeVarargs
    public static <T> LinkedList<T> of(T... values) {
        if (Arrayx.isEmpty(values)) {
            return new LinkedList<>();
        }

        LinkedList<T> list = new LinkedList<>();
        Collections.addAll(list, values);
        return list;
    }

    public static <T> LinkedList<T> of(Collection<T> collection) {
        if (isNullOrEmpty(collection)) {
            return new LinkedList<>();
        }

        return new LinkedList<>(collection);
    }

    public static <T> LinkedList<T> of(Iterable<T> iterable) {
        if (null == iterable) {
            return new LinkedList<>();
        }

        return of(iterable.iterator());
    }

    public static <T> LinkedList<T> of(Iterator<T> iterator) {
        LinkedList<T> list = new LinkedList<>();
        if (null != iterator) {
            while (iterator.hasNext()) {
                list.add(iterator.next());
            }
        }
        return list;
    }

    public static <T> LinkedList<T> of(Enumeration<T> enumeration) {
        LinkedList<T> list = new LinkedList<>();
        if (null != enumeration) {
            while (enumeration.hasMoreElements()) {
                list.add(enumeration.nextElement());
            }
        }
        return list;
    }
}
