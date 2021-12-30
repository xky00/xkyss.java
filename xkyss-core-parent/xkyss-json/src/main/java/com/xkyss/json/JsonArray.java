package com.xkyss.json;

import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.xkyss.json.JsonUtil.*;
import static java.time.format.DateTimeFormatter.ISO_INSTANT;

/**
 * Json数组
 *
 * @author xkyii
 * Create on 2021/07/27.
 */
public class JsonArray implements Iterable<Object> {

    private List<Object> list;

    public JsonArray() {
        list = new ArrayList<>();
    }

    public JsonArray(String json) {
        if (json == null) {
            throw new NullPointerException();
        }
        fromJson(json);
        if (list == null) {
            throw new DecodeException("Invalid JSON array: " + json);
        }
    }

    public JsonArray(List list) {
        if (list == null) {
            throw new NullPointerException();
        }
        this.list = list;
    }

    public String getString(int pos) {
        Object val = list.get(pos);

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

    public Number getNumber(int pos) {
        return (Number) list.get(pos);
    }

    public Integer getInteger(int pos) {
        Number number = (Number) list.get(pos);
        if (number == null) {
            return null;
        } else if (number instanceof Integer) {
            return (Integer) number; // Avoids unnecessary unbox/box
        } else {
            return number.intValue();
        }
    }

    public Long getLong(int pos) {
        Number number = (Number) list.get(pos);
        if (number == null) {
            return null;
        } else if (number instanceof Long) {
            return (Long) number; // Avoids unnecessary unbox/box
        } else {
            return number.longValue();
        }
    }

    public Double getDouble(int pos) {
        Number number = (Number) list.get(pos);
        if (number == null) {
            return null;
        } else if (number instanceof Double) {
            return (Double) number; // Avoids unnecessary unbox/box
        } else {
            return number.doubleValue();
        }
    }

    public Float getFloat(int pos) {
        Number number = (Number) list.get(pos);
        if (number == null) {
            return null;
        } else if (number instanceof Float) {
            return (Float) number; // Avoids unnecessary unbox/box
        } else {
            return number.floatValue();
        }
    }

    public Boolean getBoolean(int pos) {
        return (Boolean) list.get(pos);
    }

    public JsonObject getJsonObject(int pos) {
        Object val = list.get(pos);
        if (val instanceof Map) {
            val = new JsonObject((Map) val);
        }
        return (JsonObject) val;
    }

    public JsonArray getJsonArray(int pos) {
        Object val = list.get(pos);
        if (val instanceof List) {
            val = new JsonArray((List) val);
        }
        return (JsonArray) val;
    }

    public byte[] getBinary(int pos) {
        Object val = list.get(pos);
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

    public Instant getInstant(int pos) {
        Object val = list.get(pos);
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

    public Object getValue(int pos) {
        return wrapJsonValue(list.get(pos));
    }

    public boolean hasNull(int pos) {
        return list.get(pos) == null;
    }

    public JsonArray addNull() {
        list.add(null);
        return this;
    }

    public JsonArray add(Object value) {
        list.add(value);
        return this;
    }

    public JsonArray add(int pos, Object value) {
        list.add(pos, value);
        return this;
    }

    public JsonArray addAll(JsonArray array) {
        list.addAll(array.list);
        return this;
    }

    public JsonArray setNull(int pos) {
        list.set(pos, null);
        return this;
    }

    public JsonArray set(int pos, Object value) {
        list.set(pos, value);
        return this;
    }

    public boolean contains(Object value) {
        return list.contains(value);
    }

    public boolean remove(Object value) {
        final Object wrappedValue = wrapJsonValue(value);
        for (int i = 0; i < list.size(); i++) {
            // perform comparision on wrapped types
            final Object otherWrapperValue = getValue(i);
            if (wrappedValue == null) {
                if (otherWrapperValue == null) {
                    list.remove(i);
                    return true;
                }
            } else {
                if (wrappedValue.equals(otherWrapperValue)) {
                    list.remove(i);
                    return true;
                }
            }
        }
        return false;
    }

    public Object remove(int pos) {
        return wrapJsonValue(list.remove(pos));
    }

    public int size() {
        return list.size();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public List getList() {
        return list;
    }

    public JsonArray clear() {
        list.clear();
        return this;
    }

    @Override
    public Iterator<Object> iterator() {
        return new Iter(list.iterator());
    }

    public String encode() {
        return Json.getCodec().toString(this, false);
    }

    public String encodePrettily() {
        return Json.getCodec().toString(this, true);
    }

    public JsonArray copy() {
        return copy(DEFAULT_CLONER);
    }

    public JsonArray copy(Function<Object, ?> cloner) {
        List<Object> copiedList = new ArrayList<>(list.size());
        for (Object val : list) {
            copiedList.add(deepCopy(val, cloner));
        }
        return new JsonArray(copiedList);
    }

    public Stream<Object> stream() {
        return asStream(iterator());
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

        JsonArray other = (JsonArray) o;
        // size check
        if (this.size() != other.size())
            return false;
        // value comparison
        for (int i = 0; i < this.size(); i++) {
            Object thisValue = this.getValue(i);
            Object otherValue = other.getValue(i);
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
        return list.hashCode();
    }

    private void fromJson(String json) {
        list = Json.getCodec().fromString(json, List.class);
    }

    private static class Iter implements Iterator<Object> {

        final Iterator<Object> listIter;

        Iter(Iterator<Object> listIter) {
            this.listIter = listIter;
        }

        @Override
        public boolean hasNext() {
            return listIter.hasNext();
        }

        @Override
        public Object next() {
            return wrapJsonValue(listIter.next());
        }

        @Override
        public void remove() {
            listIter.remove();
        }
    }
}
