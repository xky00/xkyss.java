package com.xkyss.extensions.manifold.rt.api.Array;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.Self;
import manifold.ext.rt.api.This;

import java.lang.reflect.Array;
import java.util.Arrays;

@SuppressWarnings("SuspiciousSystemArraycopy")
@Extension
public class ArrayExt {

    /**
     * 数组是否不为null
     * @param array
     * @return
     */
    public static boolean isNotEmpty(@This Object array) {
        return array != null && Array.getLength(array) > 0;
    }

    @Self
    public static Object combine(@This Object array, char c) {
        if (isNullOrEmpty(array)) {
            return new char[] { c };
        }

        int len = Array.getLength(array);
        char[] s = new char[len + 1];
        System.arraycopy(array, 0, s, 0, len);
        Array.setChar(s, len, c);

        return s;
    }

    @Self
    public static Object sort(@This Object array)
    {
        Class<?> componentType = array.getClass().getComponentType();
        if( componentType.isPrimitive() )
        {
            switch( componentType.getTypeName() )
            {
                case "byte":
                    Arrays.sort((byte[])array);
                    break;
                case "short":
                    Arrays.sort((short[])array);
                    break;
                case "int":
                    Arrays.sort((int[])array);
                    break;
                case "long":
                    Arrays.sort((long[])array);
                    break;
                case "float":
                    Arrays.sort((float[])array);
                    break;
                case "double":
                    Arrays.sort((double[])array);
                    break;
                case "char":
                    Arrays.sort((char[])array);
                    break;
                default:
                    throw new UnsupportedOperationException( "Binary sort unsupported for: " + componentType );
            }
        }
        //noinspection ConstantConditions
        Arrays.sort((Object[]) array);

        return array;
    }

    private static boolean isNullOrEmpty(Object array) {
        return array == null || isEmpty(array);
    }

    private static boolean isEmpty(Object array) {
        return Array.getLength(array) == 0;
    }
}
