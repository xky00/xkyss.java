package com.xkyss.json;


import com.xkyss.json.spi.JsonCodec;
import com.xkyss.json.spi.JsonFactory;

/**
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class Json {

    /**
     * This will load the default codec.
     */
    public static final JsonCodec CODEC = load().codec();

    /**
     * Load the factory with the {@code ServiceLoader}, when no factory is found then a factory
     * using Jackson will be returned.
     * <br>
     * When {@code jackson-databind} is available then a codec using it will be used otherwise
     * the codec will only use {@code jackson-core} and provide best effort mapping.
     * @return a factory instance
     */
    public static JsonFactory load() {
        return JsonFactory.load();
    }

    /**
     * Encode a POJO to JSON using the underlying Jackson mapper.
     *
     * @param obj a POJO
     * @return a String containing the JSON representation of the given POJO.
     * @throws EncodeException if a property cannot be encoded.
     */
    public static String encode(Object obj) throws EncodeException {
        return CODEC.toString(obj);
    }

    /**
     * Encode a POJO to JSON using the underlying Jackson mapper.
     *
     * @param obj a POJO
     * @return a Buffer containing the JSON representation of the given POJO.
     * @throws EncodeException if a property cannot be encoded.
     */
//    public static Buffer encodeToBuffer(Object obj) throws EncodeException {
//        return CODEC.toBuffer(obj);
//    }

    /**
     * Encode a POJO to JSON with pretty indentation, using the underlying Jackson mapper.
     *
     * @param obj a POJO
     * @return a String containing the JSON representation of the given POJO.
     * @throws EncodeException if a property cannot be encoded.
     */
    public static String encodePrettily(Object obj) throws EncodeException {
        return CODEC.toString(obj, true);
    }

    /**
     * Decode a given JSON string to a POJO of the given class type.
     * @param str the JSON string.
     * @param clazz the class to map to.
     * @param <T> the generic type.
     * @return an instance of T
     * @throws DecodeException when there is a parsing or invalid mapping.
     */
    public static <T> T decodeValue(String str, Class<T> clazz) throws DecodeException {
        return CODEC.fromString(str, clazz);
    }

    /**
     * Decode a given JSON string.
     *
     * @param str the JSON string.
     *
     * @return a JSON element which can be a {@link JsonArray}, {@link JsonObject}, {@link String}, ...etc if the content is an array, object, string, ...etc
     * @throws DecodeException when there is a parsing or invalid mapping.
     */
    @SuppressWarnings("JavadocReference")
    public static Object decodeValue(String str) throws DecodeException {
        return decodeValue(str, Object.class);
    }

    /**
     * Decode a given JSON buffer.
     *
     * @param buf the JSON buffer.
     *
     * @return a JSON element which can be a {@link JsonArray}, {@link JsonObject}, {@link String}, ...etc if the buffer contains an array, object, string, ...etc
     * @throws DecodeException when there is a parsing or invalid mapping.
     */
//    public static Object decodeValue(Buffer buf) throws DecodeException {
//        return decodeValue(buf, Object.class);
//    }

    /**
     * Decode a given JSON buffer to a POJO of the given class type.
     * @param buf the JSON buffer.
     * @param clazz the class to map to.
     * @param <T> the generic type.
     * @return an instance of T
     * @throws DecodeException when there is a parsing or invalid mapping.
     */
//    public static <T> T decodeValue(Buffer buf, Class<T> clazz) throws DecodeException {
//        return CODEC.fromBuffer(buf, clazz);
//    }
}

