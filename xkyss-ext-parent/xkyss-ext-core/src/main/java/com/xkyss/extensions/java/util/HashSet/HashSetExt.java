package com.xkyss.extensions.java.util.HashSet;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;

import java.util.*;

@Extension
public class HashSetExt {

    @Extension
    public static <T> HashSet<T> of(T... values) {
        if (values == null) {
            return new HashSet<>();
        }

        int initialCapacity = Math.max((int) (values.length / .75f) + 1, 16);
        final HashSet<T> set = new HashSet<>(initialCapacity);
        Collections.addAll(set, values);
        return set;
    }

    @Extension
    public static <T> HashSet<T> of(Collection<T> collection) {
        if (collection == null) {
            return new HashSet<>();
        }

        return new HashSet<>(collection);
    }

    @Extension
    public static <T> HashSet<T> of(Iterable<T> iterable) {
        return of(iterable.iterator());
    }

    @Extension
    public static <T> HashSet<T> of(Iterator<T> iterator) {
        if (null == iterator) {
            return new HashSet<>();
        }
        HashSet<T> set = new HashSet<>();
        while (iterator.hasNext()) {
            set.add(iterator.next());
        }
        return set;
    }

    @Extension
    public static <T> HashSet<T> of(Enumeration<T> enumeration) {
        if (null == enumeration) {
            return new HashSet<>();
        }
        HashSet<T> set = new HashSet<>();
        while (enumeration.hasMoreElements()) {
            set.add(enumeration.nextElement());
        }
        return set;
    }
}
