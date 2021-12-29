package com.xkyss.extensions.java.util.ArrayList;

import manifold.ext.rt.api.Extension;

import java.util.*;

@Extension
public class ArrayListExt {

    @Extension
    public static <T> ArrayList<T> of(T... values) {
        if (values.isEmpty()) {
            return new ArrayList<>();
        }

        ArrayList<T> list = new ArrayList<>(values.length);
        Collections.addAll(list, values);
        return list;
    }

    @Extension
    public static <T> ArrayList<T> of(Collection<T> collection) {
        if (collection.isNullOrEmpty()) {
            return new ArrayList<>();
        }

        return new ArrayList<>(collection);
    }

    @Extension
    public static <T> ArrayList<T> of(Iterable<T> iterable) {
        if (null == iterable) {
            return new ArrayList<>();
        }

        return of(iterable.iterator());
    }

    @Extension
    public static <T> ArrayList<T> of(Iterator<T> iterator) {
        ArrayList<T> list = new ArrayList<>();
        if (null != iterator) {
            while (iterator.hasNext()) {
                list.add(iterator.next());
            }
        }
        return list;
    }

    @Extension
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
