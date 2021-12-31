package com.xkyss.core.util;

import java.util.*;

public class SetUtil extends CollectionUtil {

    /**
     * 新建一个HashSet
     *
     * @param <T> 集合元素类型
     * @param ts  元素数组
     * @return HashSet对象
     */
    @SafeVarargs
    public static <T> HashSet<T> of(T... ts) {
        return HashSet.of(ts);
    }

    /**
     * 新建一个HashSet
     *
     * @param <T> 集合元素类型
     * @param ts  元素数组
     * @return HashSet对象
     */
    @SafeVarargs
    public static <T> HashSet<T> toHashSet(T... ts) {
        return HashSet.of(ts);
    }

    /**
     * 新建一个HashSet
     *
     * @param <T>        集合元素类型
     * @param collection 集合
     * @return HashSet对象
     */
    public static <T> HashSet<T> toHashSet(Collection<T> collection) {
        return HashSet.of(collection);
    }

    /**
     * 新建一个HashSet
     *
     * @param <T>        集合元素类型
     * @param iterable  {@link Iterable}
     * @return HashSet对象
     */
    public static <T> HashSet<T> toHashSet(Iterable<T> iterable) {
        return HashSet.of(iterable);
    }

    /**
     * 新建一个HashSet
     *
     * @param <T>        集合元素类型
     * @param iterator {@link Iterator}
     * @return HashSet对象
     */
    public static <T> HashSet<T> toHashSet(Iterator<T> iterator) {
        return HashSet.of(iterator);
    }

    /**
     * 新建一个HashSet
     *
     * @param <T>        集合元素类型
     * @param enumeration {@link Enumeration}
     * @return HashSet对象
     */
    public static <T> HashSet<T> toHashSet(Enumeration<T> enumeration) {
        return HashSet.of(enumeration);
    }

    /**
     * 新建一个LinkedHashSet
     *
     * @param <T> 集合元素类型
     * @param ts  元素数组
     * @return LinkedHashSet对象
     */
    @SafeVarargs
    public static <T> LinkedHashSet<T> toLinkedHashSet(T... ts) {
        return LinkedHashSet.of(ts);
    }

    /**
     * 新建一个LinkedHashSet
     *
     * @param <T>        集合元素类型
     * @param collection 集合
     * @return LinkedHashSet对象
     */
    public static <T> LinkedHashSet<T> toLinkedHashSet(Collection<T> collection) {
        return LinkedHashSet.of(collection);
    }

    /**
     * 新建一个LinkedHashSet
     *
     * @param <T>        集合元素类型
     * @param iterable  {@link Iterable}
     * @return LinkedHashSet对象
     */
    public static <T> LinkedHashSet<T> toLinkedHashSet(Iterable<T> iterable) {
        return LinkedHashSet.of(iterable);
    }

    /**
     * 新建一个LinkedHashSet
     *
     * @param <T>        集合元素类型
     * @param iterator {@link Iterator}
     * @return LinkedHashSet对象
     */
    public static <T> LinkedHashSet<T> toLinkedHashSet(Iterator<T> iterator) {
        return LinkedHashSet.of(iterator);
    }

    /**
     * 新建一个LinkedLinkedHashSet
     *
     * @param <T>        集合元素类型
     * @param enumeration {@link Enumeration}
     * @return LinkedHashSet对象
     */
    public static <T> LinkedHashSet<T> toLinkedHashSet(Enumeration<T> enumeration) {
        return LinkedHashSet.of(enumeration);
    }
}
