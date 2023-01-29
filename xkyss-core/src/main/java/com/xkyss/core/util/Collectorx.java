package com.xkyss.core.util;

import org.apache.commons.lang3.CharUtils;

import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Collectorx {

    public static Collector<CharSequence, ?, String> joining(char delimiter) {
        return Collectors.joining(CharUtils.toString(delimiter), "", "");
    }
}
