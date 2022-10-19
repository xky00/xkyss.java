package com.xkyss.core.util;

import java.util.List;

public class Listx extends Collectionx {

    public static <E> E get(List<E> list, int index) {
        if (isNullOrEmpty(list)) {
            return null;
        }

        if (index >= list.size()) {
            return null;
        }

        return list.get(index);
    }
}
