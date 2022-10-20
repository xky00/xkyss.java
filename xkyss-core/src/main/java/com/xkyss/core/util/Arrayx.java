package com.xkyss.core.util;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.function.IntFunction;

/**
 * 数组工具
 */
public class Arrayx extends PrimitiveArrayx {

    /**
     * An empty immutable {@link Boolean} array.
     */
    public static final Boolean[] EMPTY_BOOLEAN_OBJECT_ARRAY = {};

    /**
     * An empty immutable {@link Byte} array.
     */
    public static final Byte[] EMPTY_BYTE_OBJECT_ARRAY = {};


    /**
     * An empty immutable {@link Character} array.
     */
    public static final Character[] EMPTY_CHARACTER_OBJECT_ARRAY = {};

    /**
     * An empty immutable {@link Class} array.
     */
    public static final Class<?>[] EMPTY_CLASS_ARRAY = {};

    /**
     * An empty immutable {@link Double} array.
     */
    public static final Double[] EMPTY_DOUBLE_OBJECT_ARRAY = {};

    /**
     * An empty immutable {@link Field} array.
     *
     * @since 3.10
     */
    public static final Field[] EMPTY_FIELD_ARRAY = {};

    /**
     * An empty immutable {@link Float} array.
     */
    public static final Float[] EMPTY_FLOAT_OBJECT_ARRAY = {};


    /**
     * An empty immutable {@link Integer} array.
     */
    public static final Integer[] EMPTY_INTEGER_OBJECT_ARRAY = {};

    /**
     * An empty immutable {@link Long} array.
     */
    public static final Long[] EMPTY_LONG_OBJECT_ARRAY = {};

    /**
     * An empty immutable {@link Method} array.
     *
     * @since 3.10
     */
    public static final Method[] EMPTY_METHOD_ARRAY = {};

    /**
     * An empty immutable {@link Object} array.
     */
    public static final Object[] EMPTY_OBJECT_ARRAY = {};

    /**
     * An empty immutable {@code short} array.
     */
    public static final short[] EMPTY_SHORT_ARRAY = {};

    /**
     * An empty immutable {@link Short} array.
     */
    public static final Short[] EMPTY_SHORT_OBJECT_ARRAY = {};

    /**
     * An empty immutable {@link String} array.
     */
    public static final String[] EMPTY_STRING_ARRAY = {};

    /**
     * An empty immutable {@link Throwable} array.
     */
    public static final Throwable[] EMPTY_THROWABLE_ARRAY = {};

    /**
     * An empty immutable {@link Type} array.
     */
    public static final Type[] EMPTY_TYPE_ARRAY = {};

    /**
     * index未找到
     */
    public static final int INDEX_NOT_FOUND = -1;


    /**
     * 数组是否为空
     *
     * @param <T>   数组元素类型
     * @param array 数组
     * @return 是否为空
     */
    public static <T> boolean isEmpty(T[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 新数组
     *
     * @param values 源元素
     * @return 新数组
     * @param <T> 元素类型
     */
    @SafeVarargs
    public static <T> T[] of(T... values) {
        return values;
    }

    /**
     * 将多个数组合并在一起<br>
     * 忽略null的数组
     *
     * @param <T>    数组元素类型
     * @param arrays 数组集合
     * @return 合并后的数组
     */
    @SafeVarargs
    public static <T> T[] addAll(T[]... arrays) {
        if (arrays.length == 1) {
            return arrays[0];
        }

        int length = 0;
        for (T[] array : arrays) {
            if (null != array) {
                length += array.length;
            }
        }
        T[] result = newArray(arrays.getClass().getComponentType().getComponentType(), length);

        length = 0;
        for (T[] array : arrays) {
            if (null != array) {
                System.arraycopy(array, 0, result, length, array.length);
                length += array.length;
            }
        }
        return result;
    }

    /**
     * 新建一个空数组
     *
     * @param <T>           数组元素类型
     * @param componentType 元素类型
     * @param newSize       大小
     * @return 空数组
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] newArray(Class<?> componentType, int newSize) {
        return (T[]) Array.newInstance(componentType, newSize);
    }

    public static <T> T[] setAll(final T[] array, final IntFunction<? extends T> generator) {
        if (array != null && generator != null) {
            Arrays.setAll(array, generator);
        }
        return array;
    }
}
