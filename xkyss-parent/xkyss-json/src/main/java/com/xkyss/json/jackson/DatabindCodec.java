package com.xkyss.json.jackson;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.xkyss.json.*;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Map;

public class DatabindCodec implements JsonCodec {
    private final ObjectMapper mapper = new ObjectMapper();
    private final ObjectMapper prettyMapper = new ObjectMapper();
    private final JsonFactory factory = new JsonFactory();

    public DatabindCodec() {
        initialize();
    }

    private void initialize() {
        // Non-standard JSON but we allow C style comments in our JSON
        mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);

        prettyMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        prettyMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);

        SimpleModule module = new SimpleModule();
        // custom types
        module.addSerializer(JsonObject.class, new JsonObjectSerializer());
        module.addSerializer(JsonArray.class, new JsonArraySerializer());
        // he have 2 extensions: RFC-7493
        module.addSerializer(Instant.class, new InstantSerializer());
        module.addDeserializer(Instant.class, new InstantDeserializer());
        module.addSerializer(byte[].class, new ByteArraySerializer());
        module.addDeserializer(byte[].class, new ByteArrayDeserializer());

        mapper.registerModule(module);
        prettyMapper.registerModule(module);
    }

    /**
     * @return the {@link ObjectMapper} used for data binding.
     */
    public ObjectMapper mapper() {
        return mapper;
    }

    /**
     * @return the {@link ObjectMapper} used for data binding configured for indenting output.
     */
    public ObjectMapper prettyMapper() {
        return prettyMapper;
    }

    @Override
    public <T> T fromValue(Object json, Class<T> clazz) {
        T value = mapper.convertValue(json, clazz);
        if (clazz == Object.class) {
            value = (T) adapt(value);
        }
        return value;
    }

    public <T> T fromValue(Object json, TypeReference<T> type) {
        T value = mapper.convertValue(json, type);
        if (type.getType() == Object.class) {
            value = (T) adapt(value);
        }
        return value;
    }

    @Override
    public <T> T fromString(String str, Class<T> clazz) throws DecodeException {
        return fromParser(createParser(str), clazz);
    }

    public <T> T fromString(String str, TypeReference<T> typeRef) throws DecodeException {
        return fromParser(createParser(str), typeRef);
    }

    public JsonParser createParser(String str) {
        try {
            return factory.createParser(str);
        } catch (IOException e) {
            throw new DecodeException("Failed to decode:" + e.getMessage(), e);
        }
    }

    public <T> T fromParser(JsonParser parser, Class<T> type) throws DecodeException {
        T value;
        JsonToken remaining;
        try {
            value = mapper.readValue(parser, type);
            remaining = parser.nextToken();
        } catch (Exception e) {
            throw new DecodeException("Failed to decode:" + e.getMessage(), e);
        } finally {
            JsonCodec.close(parser);
        }
        if (remaining != null) {
            throw new DecodeException("Unexpected trailing token");
        }
        if (type == Object.class) {
            value = (T) adapt(value);
        }
        return value;
    }

    private <T> T fromParser(JsonParser parser, TypeReference<T> type) throws DecodeException {
        T value;
        try {
            value = mapper.readValue(parser, type);
        } catch (Exception e) {
            throw new DecodeException("Failed to decode:" + e.getMessage(), e);
        } finally {
            JsonCodec.close(parser);
        }
        if (type.getType() == Object.class) {
            value = (T) adapt(value);
        }
        return value;
    }

    @Override
    public String toString(Object object, boolean pretty) throws EncodeException {
        try {
            ObjectMapper mapper = pretty ? this.prettyMapper : this.mapper;
            return mapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new EncodeException("Failed to encode as JSON: " + e.getMessage());
        }
    }

    private static Object adapt(Object o) {
        try {
            if (o instanceof List) {
                List list = (List) o;
                return new JsonArray(list);
            } else if (o instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> map = (Map<String, Object>) o;
                return new JsonObject(map);
            }
            return o;
        } catch (Exception e) {
            throw new DecodeException("Failed to decode: " + e.getMessage());
        }
    }
}
