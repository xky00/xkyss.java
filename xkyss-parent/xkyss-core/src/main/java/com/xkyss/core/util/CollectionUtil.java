package com.xkyss.core.util;


import java.util.*;
import java.util.stream.Stream;

/**
 * 集合工具类
 *
 * @author xkyii
 * Created on 2021/07/29.
 */
public class CollectionUtil extends IterUtil {
    /**
     * 集合是否为空
     *
     * @param collection 集合
     * @return 是否为空
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * 差集
     * 集合1中有,集合2中没有的元素
     *
     * @param c1 集合1
     * @param c2 集合2
     * @param <T> 元素类型
     * @return 差集集合流
     */
    public static <T> Stream<T> subtractToStream(Collection<T> c1, Collection<T> c2) {
        if (c1 == null) {
            return Stream.empty();
        }

        return c1.stream().subtract(c2);
    }

    /**
     * 交集
     * 集合1中有,集合2中都有的元素
     *
     * @param c1 集合1
     * @param c2 集合2
     * @param <T> 元素类型
     * @return 交集集合流
     */
    public static <T> Stream<T> intersectionToStream(Collection<T> c1, Collection<T> c2) {
        if (c1 == null) {
            return Stream.empty();
        }

        return c1.stream().intersection(c2);
    }

    /**
     * 创建新的集合对象
     *
     * @param <T>            集合类型
     * @param collectionType 集合类型
     * @return 集合类型对应的实例
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> Collection<T> create(Class<?> collectionType) {
        Collection<T> list;
        if (collectionType.isAssignableFrom(AbstractCollection.class)) {
            // 抽象集合默认使用ArrayList
            list = new ArrayList<>();
        }

        // Set
        else if (collectionType.isAssignableFrom(HashSet.class)) {
            list = new HashSet<>();
        } else if (collectionType.isAssignableFrom(LinkedHashSet.class)) {
            list = new LinkedHashSet<>();
        } else if (collectionType.isAssignableFrom(TreeSet.class)) {
            list = new TreeSet<>((o1, o2) -> {
                // 优先按照对象本身比较，如果没有实现比较接口，默认按照toString内容比较
                if (o1 instanceof Comparable) {
                    return ((Comparable<T>) o1).compareTo(o2);
                }
                return CompareUtil.compare(o1.toString(), o2.toString());
            });
        }
        // TODO: Complete Me!
//        else if (collectionType.isAssignableFrom(EnumSet.class)) {
//            list = (Collection<T>) EnumSet.noneOf((Class<Enum>) ClassUtil.getTypeArgument(collectionType));
//        }

        // List
        else if (collectionType.isAssignableFrom(ArrayList.class)) {
            list = new ArrayList<>();
        } else if (collectionType.isAssignableFrom(LinkedList.class)) {
            list = new LinkedList<>();
        }

        // Others，直接实例化
        else {
//            try {
//                list = (Collection<T>) ReflectUtil.newInstance(collectionType);
//            } catch (Exception e) {
//                // 无法创建当前类型的对象，尝试创建父类型对象
//                final Class<?> superclass = collectionType.getSuperclass();
//                if(null != superclass && collectionType != superclass){
//                    return create(superclass);
//                }
//                throw new UnsupportedOperationException(e);
//            }
            return new ArrayList<>();
        }
        return list;
    }
}
