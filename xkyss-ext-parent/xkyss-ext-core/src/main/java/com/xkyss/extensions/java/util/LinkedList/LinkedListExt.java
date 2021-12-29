package com.xkyss.extensions.java.util.LinkedList;

import manifold.ext.rt.api.Extension;

import java.util.*;


@Extension
public class LinkedListExt {

    @Extension
    public static <T> LinkedList<T> of(T... values) {
        if (values.isEmpty()) {
            return new LinkedList<>();
        }

        LinkedList<T> list = new LinkedList<>();
        Collections.addAll(list, values);
        return list;
    }

    @Extension
    public static <T> LinkedList<T> of(Collection<T> collection) {
        if (collection.isNullOrEmpty()) {
            return new LinkedList<>();
        }

        return new LinkedList<>(collection);
    }

    @Extension
    public static <T> LinkedList<T> of(Iterable<T> iterable) {
        if (null == iterable) {
            return new LinkedList<>();
        }

        return of(iterable.iterator());
    }

    @Extension
    public static <T> LinkedList<T> of(Iterator<T> iterator) {
        LinkedList<T> list = new LinkedList<>();
        if (null != iterator) {
            while (iterator.hasNext()) {
                list.add(iterator.next());
            }
        }
        return list;
    }

    @Extension
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
