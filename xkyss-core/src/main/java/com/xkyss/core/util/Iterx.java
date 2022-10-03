package com.xkyss.core.util;

import java.util.Iterator;

/**
 * {@link Iterable} 和 {@link Iterator} 相关工具类
 *
 * @author xkyii
 * Created on 2021/07/29.
 */
public class Iterx {

    /**
     * Iterable是否为空
     *
     * @param iterable Iterable对象
     * @return 是否为空
     */
    public static boolean isEmpty(Iterable<?> iterable) {
        return null == iterable || isEmpty(iterable.iterator());
    }

    /**
     * Iterator是否为空
     *
     * @param Iterator Iterator对象
     * @return 是否为空
     */
    public static boolean isEmpty(Iterator<?> Iterator) {
        return null == Iterator || !Iterator.hasNext();
    }

}
