package com.xkyss.extensions.java.util.Iterator;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;

import java.util.Iterator;

@Extension
public class IteratorExt {

    public static <T> boolean isNullOrEmpty(@This Iterator<T> self) {
        return self == null || !self.hasNext();
    }
}
