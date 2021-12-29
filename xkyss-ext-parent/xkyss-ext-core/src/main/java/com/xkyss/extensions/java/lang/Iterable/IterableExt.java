package com.xkyss.extensions.java.lang.Iterable;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;

import java.lang.Iterable;

@Extension
public class IterableExt {

    public static <T> boolean isNullOrEmpty(@This Iterable<T> self) {
        return self == null || self.iterator().isNullOrEmpty();
    }
}
