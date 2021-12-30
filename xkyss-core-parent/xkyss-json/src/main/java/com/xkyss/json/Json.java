package com.xkyss.json;

import com.xkyss.json.jackson.DatabindCodec;

/**
 * Json
 *
 * @author xkyii
 * Create on 2021/07/27.
 */
public class Json {
    /**
     * 默认codec
     */
    private static final JsonCodec DefaultCodec = new DatabindCodec();

    /**
     * 运行时codec
     */
    private static JsonCodec RuntimeCodec;

    public static void setCodec(JsonCodec codec) {
        RuntimeCodec = codec;
    }

    /**
     * 获取codec,如果没有set过,就用默认的
     * @return codec
     */
    public static JsonCodec getCodec() {
        if (RuntimeCodec != null) {
            return RuntimeCodec;
        }
        return DefaultCodec;
    }

    public static String encode(Object obj) throws EncodeException {
        return DefaultCodec.toString(obj);
    }

    public static String encodePrettily(Object obj) throws EncodeException {
        return DefaultCodec.toString(obj, true);
    }

    public static <T> T decodeValue(String str, Class<T> clazz) throws DecodeException {
        return DefaultCodec.fromString(str, clazz);
    }

    public static Object decodeValue(String str) throws DecodeException {
        return decodeValue(str, Object.class);
    }

    public static <T> T decodeValue(Object obj, Class<T> clazz) throws DecodeException {
        return DefaultCodec.fromValue(obj, clazz);
    }
}
