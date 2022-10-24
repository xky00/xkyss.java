package com.xkyss.json.jackson;


import com.xkyss.json.spi.JsonCodec;
import com.xkyss.json.spi.JsonFactory;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class JacksonFactory implements JsonFactory {

    public static final JacksonFactory INSTANCE = new JacksonFactory();

    public static final JacksonCodec CODEC;

    static {
        JacksonCodec codec;
        try {
            codec = new DatabindCodec();
        } catch (Throwable ignore) {
            // No databind
            codec = new JacksonCodec();
        }
        CODEC = codec;
    }

    @Override
    public JsonCodec codec() {
        return CODEC;
    }
}

