package com.xkyss.core.util;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

public class Listx {

    /**
     * 安全获取元素
     * @param list 列表
     * @param index 索引
     * @return 获取到的元素,如果越界,则返回<code>null</code>
     * @param <E> 泛型类型
     */
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
