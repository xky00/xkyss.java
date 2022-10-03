package com.xkyss.core.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class Listx {

    ///< Region of

    /**
     * 新数组 (默认ArrayList)
     *
     * @param values 源值
     * @return 新数组
     * @param <T> 源类型
     */
    @SafeVarargs
    public static <T> ArrayList<T> of(T... values) {
        if (Arrayx.isEmpty(values)) {
            return new ArrayList<>();
        }

        ArrayList<T> list = new ArrayList<>();
        Collections.addAll(list, values);
        return list;
    }

    /**
     * 新数组 (ArrayList)
     *
     * @param values 源值
     * @return 新数组
     * @param <T> 源类型
     */
    @SafeVarargs
    public static <T> ArrayList<T> ofArrayList(T... values) {
        if (Arrayx.isEmpty(values)) {
            return new ArrayList<>();
        }

        ArrayList<T> list = new ArrayList<>();
        Collections.addAll(list, values);
        return list;
    }

    /**
     * 新数组 (LinkedList)
     *
     * @param values 源值
     * @return 新数组
     * @param <T> 源类型
     */
    @SafeVarargs
    public static <T> LinkedList<T> ofLinkedList(T... values) {
        if (Arrayx.isEmpty(values)) {
            return new LinkedList<>();
        }

        LinkedList<T> list = new LinkedList<>();
        Collections.addAll(list, values);
        return list;
    }

    ///> Region of
}
