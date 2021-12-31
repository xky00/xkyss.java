package com.xkyss.core.util;

import java.util.*;
import java.util.stream.Collectors;

/**
 * List 工具类
 *
 * @author xkyii
 * @createdAt 2020/08/05.
 */
@SuppressWarnings("unused")
public class ListUtil extends CollectionUtil {

    /**
     * 新建一个ArrayList
     *
     * @param <T>    集合元素类型
     * @param values 数组
     * @return ArrayList对象
     */
    @SafeVarargs
    public static <T> ArrayList<T> toList(T... values) {
        return ArrayList.of(values);
    }

    /**
     * 新建一个ArrayList
     *
     * @param <T>        集合元素类型
     * @param collection 集合
     * @return ArrayList对象
     */
    public static <T> ArrayList<T> toList(Collection<T> collection) {
        return ArrayList.of(collection);
    }

    /**
     * 新建一个ArrayList<br>
     * 提供的参数为null时返回空{@link ArrayList}
     *
     * @param <T>      集合元素类型
     * @param iterable {@link Iterable}
     * @return ArrayList对象
     */
    public static <T> ArrayList<T> toList(Iterable<T> iterable) {
        return ArrayList.of(iterable);
    }

    /**
     * 新建一个ArrayList<br>
     * 提供的参数为null时返回空{@link ArrayList}
     *
     * @param <T>      集合元素类型
     * @param iterator {@link Iterator}
     * @return ArrayList对象
     */
    public static <T> ArrayList<T> toList(Iterator<T> iterator) {
        return ArrayList.of(iterator);
    }

    /**
     * 新建一个ArrayList<br>
     * 提供的参数为null时返回空{@link ArrayList}
     *
     * @param <T>         集合元素类型
     * @param enumeration {@link Enumeration}
     * @return ArrayList对象
     */
    public static <T> ArrayList<T> toList(Enumeration<T> enumeration) {
        return ArrayList.of(enumeration);
    }

    /**
     * 新建一个LinkedList
     *
     * @param <T>    集合元素类型
     * @param values 数组
     * @return LinkedList对象
     */
    @SafeVarargs
    public static <T> LinkedList<T> toLinkedList(T... values) {
        return LinkedList.of(values);
    }

    /**
     * 新建一个LinkedList
     *
     * @param <T>        集合元素类型
     * @param collection 集合
     * @return LinkedList对象
     */
    public static <T> LinkedList<T> toLinkedList(Collection<T> collection) {
        return LinkedList.of(collection);
    }

    /**
     * 新建一个LinkedList<br>
     * 提供的参数为null时返回空{@link LinkedList}
     *
     * @param <T>      集合元素类型
     * @param iterable {@link Iterable}
     * @return LinkedList对象
     */
    public static <T> LinkedList<T> toLinkedList(Iterable<T> iterable) {
        return LinkedList.of(iterable);
    }

    /**
     * 新建一个LinkedList<br>
     * 提供的参数为null时返回空{@link LinkedList}
     *
     * @param <T>      集合元素类型
     * @param iterator {@link Iterator}
     * @return LinkedList对象
     */
    public static <T> LinkedList<T> toLinkedList(Iterator<T> iterator) {
        return LinkedList.of(iterator);
    }

    /**
     * 新建一个LinkedList<br>
     * 提供的参数为null时返回空{@link LinkedList}
     *
     * @param <T>         集合元素类型
     * @param enumeration {@link Enumeration}
     * @return LinkedList对象
     */
    public static <T> LinkedList<T> toLinkedList(Enumeration<T> enumeration) {
        return LinkedList.of(enumeration);
    }

    /**
     * 数组转为一个ArrayList<br>
     *
     * @param ts  对象
     * @param <T> 对象类型
     * @return 不可修改List
     */
    @SafeVarargs
    public static <T> List<T> of(T... ts) {
        return ArrayList.of(ts);
    }

    /**
     * 差集
     * 集合1中有,集合2中没有的元素
     *
     * @param c1 集合1
     * @param c2 集合2
     * @param <T> 元素类型
     * @return 差集列表
     */
    public static <T> List<T> subtract(List<T> c1, List<T> c2) {
        return subtractToStream(c1, c2).collect(Collectors.toList());
    }

    /**
     * 交集
     * 集合1中有,集合2中都有的元素
     *
     * @param c1 集合1
     * @param c2 集合2
     * @param <T> 元素类型
     * @return 交集列表
     */
    public static <T> List<T> intersection(List<T> c1, List<T> c2) {
        return intersectionToStream(c1, c2).collect(Collectors.toList());
    }
}
