package com.xkyss.json.spi;


import com.xkyss.json.DecodeException;
import com.xkyss.json.EncodeException;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public interface JsonCodec {

    /**
     * Decode the provide {@code json} string to an object extending {@code clazz}.
     *
     * @param json the json string
     * @param clazz the required object's class
     * @return the instance
     * @throws DecodeException anything preventing the decoding
     */
    <T> T fromString(String json, Class<T> clazz) throws DecodeException;

    /**
     * Like {@link #fromString(String, Class)} but with a json {@link Buffer}
     */
//    <T> T fromBuffer(Buffer json, Class<T> clazz) throws DecodeException;

    /**
     * Like {@link #fromString(String, Class)} but with a json {@code Object}
     */
    <T> T fromValue(Object json, Class<T> toValueType);

    /**
     * Encode the specified {@code object} to a string.
     */
    default String toString(Object object) throws EncodeException {
        return toString(object, false);
    }

    /**
     * Encode the specified {@code object} to a string.
     *
     * @param object the object to encode
     * @param pretty {@code true} to format the string prettily
     * @return the json encoded string
     * @throws DecodeException anything preventing the encoding
     */
    String toString(Object object, boolean pretty) throws EncodeException;

    /**
     * Like {@link #toString(Object, boolean)} but with a json {@link Buffer}
     */
//    Buffer toBuffer(Object object, boolean pretty) throws EncodeException;

    /**
     * Like {@link #toString(Object)} but with a json {@link Buffer}
     */
//    default Buffer toBuffer(Object object) throws EncodeException {
//        return toBuffer(object, false);
//    }
}
