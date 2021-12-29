package com.xkyss.extensions.java.util.stream.Stream;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;

import java.util.Collection;
import java.util.LinkedList;
import java.util.stream.Stream;

@Extension
public class StreamExt {

    /**
     * 两个集合的差集<br>
     * 针对一个集合中存在多个相同元素的情况，计算两个集合中此元素的个数，保留两个集合中此元素个数差的个数<br>
     * 例如：
     *
     * <pre>
     *     disjunction([a, b, c, c, c], [a, b, c, c]) -》 [c]
     *     disjunction([a, b], [])                    -》 [a, b]
     *     disjunction([a, b, c], [b, c, d])          -》 [a, d]
     * </pre>
     * 任意一个集合为空，返回另一个集合<br>
     * 两个集合无差集则返回空集合
     *
     * @param <T>   集合元素类型
     * @param self  集合1
     * @param c2    集合2
     * @return 差集流
     */
    public static <T> Stream<T> disjunction(@This Stream<T> self, Collection<T> c2) {
        if (self == null) {
            return Stream.empty();
        }

        if (c2 == null) {
            return self;
        }

        Collection<T> t = new LinkedList<>(c2);
        return self.filter(e -> !t.remove(e));
    }

    /**
     * 计算集合的单差集，即只返回【集合1】中有，但是【集合2】中没有的元素，例如：
     *
     * <pre>
     *     subtract([1,2,3,4],[2,3,4,5]) -》 [1]
     * </pre>
     *
     * @param self  集合1
     * @param c2    集合2
     * @param <T>   元素类型
     * @return 单差集流
     */
    public static <T> Stream<T> subtract(@This Stream<T> self, Collection<T> c2) {
        if (self == null) {
            return Stream.empty();
        }

        if (c2 == null) {
            return self;
        }

        return self.filter(e -> !c2.contains(e));
    }

    /**
     * 两个集合的交集<br>
     * 针对一个集合中存在多个相同元素的情况，计算两个集合中此元素的个数，保留最少的个数<br>
     * 例如：集合1：[a, b, c, c, c]，集合2：[a, b, c, c]<br>
     * 结果：[a, b, c, c]，此结果中只保留了两个c
     *
     * @param <T>   集合元素类型
     * @param self  集合1
     * @param c2    集合2
     * @return 交集流
     */
    public static <T> Stream<T> intersection(@This Stream<T> self, Collection<T> c2) {
        if (self == null || c2 == null) {
            return Stream.empty();
        }

        Collection<T> t = new LinkedList<>(c2);
        return self.filter(t::remove);
    }

    /**
     * 多个集合的交集<br>
     * 针对一个集合中存在多个相同元素的情况，计算两个集合中此元素的个数，保留最少的个数<br>
     * 例如：集合1：[a, b, c, c, c]，集合2：[a, b, c, c]<br>
     * 结果：[a, b, c, c]，此结果中只保留了两个c
     *
     * @param <T>       集合元素类型
     * @param self      集合1
     * @param c2        集合2
     * @param cs        其它集合
     * @return 交集流
     */
    @SafeVarargs
    public static <T> Stream<T> intersection(@This Stream<T> self, Collection<T> c2, Collection<T>... cs) {
        Stream<T> stream = self.intersection(c2);

        if (cs.isNullOrEmpty()) {
            return stream;
        }

        for (Collection<T> c: cs) {
            stream = stream.intersection(c);
        }

        return stream;
    }

    /**
     * 两个集合的交集<br>
     * 针对一个集合中存在多个相同元素的情况，只保留一个<br>
     * 例如：集合1：[a, b, c, c, c]，集合2：[a, b, c, c]<br>
     * 结果：[a, b, c]，此结果中只保留了一个c
     *
     * @param <T>       集合元素类型
     * @param self      集合1
     * @param c2        集合2
     * @return 交集流
     */
    public static <T> Stream<T> intersectionDistinct(@This Stream<T> self, Collection<T> c2) {
        if (self == null || c2 == null) {
            return Stream.empty();
        }

        return self.distinct().filter(c2::contains);
    }

    /**
     * 多个集合的交集<br>
     * 针对一个集合中存在多个相同元素的情况，只保留一个<br>
     * 例如：集合1：[a, b, c, c, c]，集合2：[a, b, c, c]<br>
     * 结果：[a, b, c]，此结果中只保留了一个c
     *
     * @param <T>       集合元素类型
     * @param self      集合1
     * @param c2        集合2
     * @param cs        其它集合
     * @return 交集流
     */
    @SafeVarargs
    public static <T> Stream<T> intersectionDistinct(@This Stream<T> self, Collection<T> c2, Collection<T>... cs) {
        Stream<T> stream = self.intersectionDistinct(c2);

        if (cs.isNullOrEmpty()) {
            return stream;
        }

        for (Collection<T> c: cs) {
            stream = stream.filter(c::contains);
        }

        return stream;
    }
}
