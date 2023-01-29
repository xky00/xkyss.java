package com.xkyss.core.util;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

public class Listx {

    public static <E> E get(List<E> list, int index) {
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }

        if (index >= list.size()) {
            return null;
        }

        return list.get(index);
    }
}
