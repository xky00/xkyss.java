package com.xkyss.core.stream;

import com.xkyss.core.util.Charx;

import java.util.stream.Collector;
import java.util.stream.Collectors;

public final class Collectorx {
    public static Collector<CharSequence, ?, String> joining(char delimiter) {
        return Collectors.joining(Charx.toString(delimiter), "", "");
    }
}
