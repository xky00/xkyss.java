package com.xkyss.extensions.java.util.Collection;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@Extension
public class CollectionExt {

    public static <E> List<E> toList(@This Collection<E> self) {
        return new ArrayList<>( self );
    }

    @Extension
    public static <E> List<E> of(E... values) {
        if (values == null) {
            return new ArrayList<>();
        }

        int initialCapacity = Math.max((int) (values.length / .75f) + 1, 16);
        final ArrayList<E> list = new ArrayList<>(initialCapacity);
        Collections.addAll(list, values);
        return list;
    }

}
