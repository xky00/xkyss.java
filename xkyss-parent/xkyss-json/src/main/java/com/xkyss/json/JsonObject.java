package com.xkyss.json;

import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.xkyss.json.JsonUtil.*;
import static java.time.format.DateTimeFormatter.ISO_INSTANT;

/**
 * Json对象
 *
 * @author xkyii
 * Create on 2021/07/27.
 */
public class JsonObject implements Iterable<Map.Entry<String, Object>> {

    private Map<String, Object> map;

    private JsonCodec codec;

    /**
     * 创建一个空Json对象
     */
    public JsonObject() {
        map = new LinkedHashMap<>();
    }

    /**
     * 从字符串创建一个Json对象
     *
     * @param json the string of JSON
     */
    public JsonObject(String json) {
        if (json == null) {
            throw new NullPointerException();
        }
        fromJson(json);
        if (map == null) {
            throw new DecodeException("Invalid JSON object: " + json);
        }
    }

    public JsonObject(Map<String, Object> map) {
        if (map == null) {
            throw new NullPointerException();
        }
        this.map = map;
    }

    public static JsonObject mapFrom(Object obj) {
        return mapFrom(obj, Json.getCodec());
    }

    @SuppressWarnings("unchecked")
    public static JsonObject mapFrom(Object obj, JsonCodec codec) {
        if (obj == null) {
            return null;
        } else {
            JsonCodec jc = (codec == null) ? Json.getCodec() : codec;
            return new JsonObject((Map<String, Object>) jc.fromValue(obj, Map.class));
        }
    }

    public <T> T mapTo(Class<T> type) {
        return getCodec().fromValue(map, type);
    }

    public String getString(String key) {
        Objects.requireNonNull(key);
        Object val = map.get(key);
        if (val == null) {
            return null;
        }

        if (val instanceof Instant) {
            return ISO_INSTANT.format((Instant) val);
        } else if (val instanceof byte[]) {
            return BASE64_ENCODER.encodeToString((byte[]) val);
        } else if (val instanceof Enum) {
            return ((Enum) val).name();
        } else {
            return val.toString();
        }
    }

    public Number getNumber(String key) {
        Objects.requireNonNull(key);
        return (Number) map.get(key);
    }

    public Integer getInteger(String key) {
        Objects.requireNonNull(key);
        Number number = (Number) map.get(key);
        if (number == null) {
            return null;
        } else if (number instanceof Integer) {
            return (Integer) number;  // Avoids unnecessary unbox/box
        } else {
            return number.intValue();
        }
    }

    public Long getLong(String key) {
        Objects.requireNonNull(key);
        Number number = (Number) map.get(key);
        if (number == null) {
            return null;
        } else if (number instanceof Long) {
            return (Long) number;  // Avoids unnecessary unbox/box
        } else {
            return number.longValue();
        }
    }

    public Double getDouble(String key) {
        Objects.requireNonNull(key);
        Number number = (Number) map.get(key);
        if (number == null) {
            return null;
        } else if (number instanceof Double) {
            return (Double) number;  // Avoids unnecessary unbox/box
        } else {
            return number.doubleValue();
        }
    }

    public Float getFloat(String key) {
        Objects.requireNonNull(key);
        Number number = (Number) map.get(key);
        if (number == null) {
            return null;
        } else if (number instanceof Float) {
            return (Float) number;  // Avoids unnecessary unbox/box
        } else {
            return number.floatValue();
        }
    }

    public Boolean getBoolean(String key) {
        Objects.requireNonNull(key);
        return (Boolean) map.get(key);
    }

    public JsonObject getJsonObject(String key) {
        Objects.requireNonNull(key);
        Object val = map.get(key);
        if (val instanceof Map) {
            val = new JsonObject((Map) val);
        }
        return (JsonObject) val;
    }

    public JsonArray getJsonArray(String key) {
        Objects.requireNonNull(key);
        Object val = map.get(key);
        if (val instanceof List) {
            val = new JsonArray((List) val);
        }
        return (JsonArray) val;
    }


    public byte[] getBinary(String key) {
        Objects.requireNonNull(key);
        Object val = map.get(key);
        // no-op
        if (val == null) {
            return null;
        }
        // no-op if value is already an byte[]
        if (val instanceof byte[]) {
            return (byte[]) val;
        }
        // assume that the value is in String format as per RFC
        String encoded = (String) val;
        // parse to proper type
        return BASE64_DECODER.decode(encoded);
    }


    public Instant getInstant(String key) {
        Objects.requireNonNull(key);
        Object val = map.get(key);
        // no-op
        if (val == null) {
            return null;
        }
        // no-op if value is already an Instant
        if (val instanceof Instant) {
            return (Instant) val;
        }
        // assume that the value is in String format as per RFC
        String encoded = (String) val;
        // parse to proper type
        return Instant.from(ISO_INSTANT.parse(encoded));
    }

    public Object getValue(String key) {
        Objects.requireNonNull(key);
        return wrapJsonValue(map.get(key));
    }

    public String getString(String key, String def) {
        Objects.requireNonNull(key);
        if (map.containsKey(key)) {
            return getString(key);
        } else {
            return def;
        }
    }

    public Number getNumber(String key, Number def) {
        Objects.requireNonNull(key);
        if (map.containsKey(key)) {
            return getNumber(key);
        } else {
            return def;
        }
    }

    public Integer getInteger(String key, Integer def) {
        Objects.requireNonNull(key);
        if (map.containsKey(key)) {
            return getInteger(key);
        } else {
            return def;
        }
    }

    public Long getLong(String key, Long def) {
        Objects.requireNonNull(key);
        if (map.containsKey(key)) {
            return getLong(key);
        } else {
            return def;
        }
    }

    public Double getDouble(String key, Double def) {
        Objects.requireNonNull(key);
        if (map.containsKey(key)) {
            return getDouble(key);
        } else {
            return def;
        }
    }

    public Float getFloat(String key, Float def) {
        Objects.requireNonNull(key);
        if (map.containsKey(key)) {
            return getFloat(key);
        } else {
            return def;
        }
    }

    public Boolean getBoolean(String key, Boolean def) {
        Objects.requireNonNull(key);
        if (map.containsKey(key)) {
            return getBoolean(key);
        } else {
            return def;
        }
    }

    public JsonObject getJsonObject(String key, JsonObject def) {
        Objects.requireNonNull(key);
        if (map.containsKey(key)) {
            return getJsonObject(key);
        } else {
            return def;
        }
    }

    public JsonArray getJsonArray(String key, JsonArray def) {
        Objects.requireNonNull(key);
        if (map.containsKey(key)) {
            return getJsonArray(key);
        } else {
            return def;
        }
    }

    public byte[] getBinary(String key, byte[] def) {
        Objects.requireNonNull(key);
        if (map.containsKey(key)) {
            return getBinary(key);
        } else {
            return def;
        }
    }

    public Instant getInstant(String key, Instant def) {
        Objects.requireNonNull(key);
        if (map.containsKey(key)) {
            return getInstant(key);
        } else {
            return def;
        }
    }

    public Object getValue(String key, Object def) {
        Objects.requireNonNull(key);
        if (map.containsKey(key)) {
            return getValue(key);
        } else {
            return def;
        }
    }

    public boolean containsKey(String key) {
        Objects.requireNonNull(key);
        return map.containsKey(key);
    }

    public Set<String> fieldNames() {
        return map.keySet();
    }

    public JsonObject putNull(String key) {
        Objects.requireNonNull(key);
        map.put(key, null);
        return this;
    }

    public JsonObject put(String key, Object value) {
        Objects.requireNonNull(key);
        map.put(key, value);
        return this;
    }

    public Object remove(String key) {
        Objects.requireNonNull(key);
        return wrapJsonValue(map.remove(key));
    }

    public JsonObject mergeIn(JsonObject other) {
        return mergeIn(other, false);
    }

    public JsonObject mergeIn(JsonObject other, boolean deep) {
        return mergeIn(other, deep ? Integer.MAX_VALUE : 1);
    }

    @SuppressWarnings("unchecked")
    public JsonObject mergeIn(JsonObject other, int depth) {
        if (depth < 1) {
            return this;
        }
        if (depth == 1) {
            map.putAll(other.map);
            return this;
        }
        for (Map.Entry<String, Object> e : other.map.entrySet()) {
            if (e.getValue() == null) {
                map.put(e.getKey(), null);
            } else {
                map.merge(e.getKey(), e.getValue(), (oldVal, newVal) -> {
                    if (oldVal instanceof Map) {
                        oldVal = new JsonObject((Map) oldVal);
                    }
                    if (newVal instanceof Map) {
                        newVal = new JsonObject((Map) newVal);
                    }
                    if (oldVal instanceof JsonObject && newVal instanceof JsonObject) {
                        return ((JsonObject) oldVal).mergeIn((JsonObject) newVal, depth - 1);
                    }
                    return newVal;
                });
            }
        }
        return this;
    }

    public String encode() {
        return getCodec().toString(this, false);
    }


    public String encodePrettily() {
        return getCodec().toString(this, true);
    }


    public JsonObject copy() {
        return copy(DEFAULT_CLONER);
    }

    public JsonObject copy(Function<Object, ?> cloner) {
        Map<String, Object> copiedMap;
        if (map instanceof LinkedHashMap) {
            copiedMap = new LinkedHashMap<>(map.size());
        } else {
            copiedMap = new HashMap<>(map.size());
        }
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Object val = deepCopy(entry.getValue(), cloner);
            copiedMap.put(entry.getKey(), val);
        }
        return new JsonObject(copiedMap);
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public Stream<Map.Entry<String, Object>> stream() {
        return asStream(iterator());
    }

    @Override
    public Iterator<Map.Entry<String, Object>> iterator() {
        return new Iter(map.entrySet().iterator());
    }

    public int size() {
        return map.size();
    }

    public JsonObject clear() {
        map.clear();
        return this;
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public String toString() {
        return encode();
    }

    @Override
    public boolean equals(Object o) {
        // null check
        if (o == null)
            return false;
        // self check
        if (this == o)
            return true;
        // type check and cast
        if (getClass() != o.getClass())
            return false;

        JsonObject other = (JsonObject) o;
        // size check
        if (this.size() != other.size())
            return false;
        // value comparison
        for (String key : map.keySet()) {
            if (!other.containsKey(key)) {
                return false;
            }

            Object thisValue = this.getValue(key);
            Object otherValue = other.getValue(key);
            // identity check
            if (thisValue == otherValue) {
                continue;
            }
            // special case for numbers
            if (thisValue instanceof Number && otherValue instanceof Number && thisValue.getClass() != otherValue.getClass()) {
                Number n1 = (Number) thisValue;
                Number n2 = (Number) otherValue;
                // floating point values
                if (thisValue instanceof Float || thisValue instanceof Double || otherValue instanceof Float || otherValue instanceof Double) {
                    // compare as floating point double
                    if (n1.doubleValue() == n2.doubleValue()) {
                        // same value check the next entry
                        continue;
                    }
                }
                if (thisValue instanceof Integer || thisValue instanceof Long || otherValue instanceof Integer || otherValue instanceof Long) {
                    // compare as integer long
                    if (n1.longValue() == n2.longValue()) {
                        // same value check the next entry
                        continue;
                    }
                }
            }
            // special case for char sequences
            if (thisValue instanceof CharSequence && otherValue instanceof CharSequence && thisValue.getClass() != otherValue.getClass()) {
                CharSequence s1 = (CharSequence) thisValue;
                CharSequence s2 = (CharSequence) otherValue;

                if (Objects.equals(s1.toString(), s2.toString())) {
                    // same value check the next entry
                    continue;
                }
            }
            // fallback to standard object equals checks
            if (!Objects.equals(thisValue, otherValue)) {
                return false;
            }
        }
        // all checks passed
        return true;
    }

    @Override
    public int hashCode() {
        return map.hashCode();
    }

    private void fromJson(String json) {
        map = getCodec().fromString(json, Map.class);
    }

    public void setCodec(JsonCodec codec) {
        this.codec = codec;
    }

    public JsonCodec getCodec() {
        if (this.codec != null) {
            return this.codec;
        }

        return Json.getCodec();
    }

    private static class Iter implements Iterator<Map.Entry<String, Object>> {

        final Iterator<Map.Entry<String, Object>> mapIter;

        Iter(Iterator<Map.Entry<String, Object>> mapIter) {
            this.mapIter = mapIter;
        }

        @Override
        public boolean hasNext() {
            return mapIter.hasNext();
        }

        @Override
        public Map.Entry<String, Object> next() {
            final Map.Entry<String, Object> entry = mapIter.next();
            final Object val = entry.getValue();
            // perform wrapping
            final Object wrapped = wrapJsonValue(val);

            if (val != wrapped) {
                return new Entry(entry.getKey(), wrapped);
            }

            return entry;
        }

        @Override
        public void remove() {
            mapIter.remove();
        }
    }

    private static final class Entry implements Map.Entry<String, Object> {
        final String key;
        final Object value;

        public Entry(String key, Object value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String getKey() {
            return key;
        }

        @Override
        public Object getValue() {
            return value;
        }

        @Override
        public Object setValue(Object value) {
            throw new UnsupportedOperationException();
        }
    }
}
