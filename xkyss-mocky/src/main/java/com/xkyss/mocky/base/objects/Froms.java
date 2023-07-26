package com.xkyss.mocky.base.objects;

import com.xkyss.mocky.abstraction.MockUnit;

import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.xkyss.mocky.contant.MockConsts.INPUT_PARAMETER_NOT_EMPTY_OR_NULL;
import static org.apache.commons.lang3.Validate.notEmpty;
import static org.apache.commons.lang3.Validate.notNull;

public class Froms {
    private final Random random;

    public Froms(Random random) {
        this.random = random;
    }

    public <T> MockUnit<T> from(List<T> alphabet) {
        return () -> {
            int idx = random.nextInt(alphabet.size());
            return alphabet.get(idx);
        };
    }

    public <T> MockUnit<T> from(T[] alphabet) {
        return () -> {
            int idx = random.nextInt(alphabet.length);
            return alphabet[idx];
        };
    }

    public <T extends Enum<?>> MockUnit<T> from(Class<T> enumClass) {
        notNull(enumClass, "enumClass");

        T[] arr = enumClass.getEnumConstants();
        return from(arr);
    }

    public <T> MockUnit<T> fromKeys(Map<T, ?> map) {
        notEmpty(map, INPUT_PARAMETER_NOT_EMPTY_OR_NULL, "map");

        return () -> {
            //noinspection unchecked
            T[] keys = (T[]) map.keySet().toArray();
            int idx = random.nextInt(keys.length);
            return keys[idx];
        };
    }

    public <T> MockUnit<T> fromValues(Map<?, T> map) {
        notEmpty(map, INPUT_PARAMETER_NOT_EMPTY_OR_NULL, "map");
        return () -> {
            //noinspection unchecked
            T[] values = (T[]) map.values().toArray();
            int idx = random.nextInt(values.length);
            return values[idx];
        };
    }
}
