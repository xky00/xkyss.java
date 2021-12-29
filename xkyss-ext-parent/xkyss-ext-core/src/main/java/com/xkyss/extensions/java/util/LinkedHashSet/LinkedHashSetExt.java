package com.xkyss.extensions.java.util.LinkedHashSet;

import manifold.ext.rt.api.Extension;

import java.util.*;

@Extension
public class LinkedHashSetExt {
    @Extension
    public static <T> LinkedHashSet<T> of(T... values) {
        if (values == null) {
            return new LinkedHashSet<>();
        }

        int initialCapacity = Math.max((int) (values.length / .75f) + 1, 16);
        final LinkedHashSet<T> set = new LinkedHashSet<>(initialCapacity);
        Collections.addAll(set, values);
        return set;
    }

    @Extension
    public static <T> LinkedHashSet<T> of(Collection<T> collection) {
        if (collection == null) {
            return new LinkedHashSet<>();
        }

        return new LinkedHashSet<>(collection);
    }

    @Extension
    public static <T> LinkedHashSet<T> of(Iterable<T> iterable) {
        return of(iterable.iterator());
    }

    @Extension
    public static <T> LinkedHashSet<T> of(Iterator<T> iterator) {
        if (null == iterator) {
            return new LinkedHashSet<>();
        }
        LinkedHashSet<T> set = new LinkedHashSet<>();
        while (iterator.hasNext()) {
            set.add(iterator.next());
        }
        return set;
    }

    @Extension
    public static <T> LinkedHashSet<T> of(Enumeration<T> enumeration) {
        if (null == enumeration) {
            return new LinkedHashSet<>();
        }
        LinkedHashSet<T> set = new LinkedHashSet<>();
        while (enumeration.hasMoreElements()) {
            set.add(enumeration.nextElement());
        }
        return set;
    }
}
