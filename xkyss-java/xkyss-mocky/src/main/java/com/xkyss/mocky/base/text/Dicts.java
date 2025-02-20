package com.xkyss.mocky.base.text;

import com.xkyss.mocky.abstraction.ALineDictUnit;
import com.xkyss.mocky.abstraction.MockUnit;
import com.xkyss.mocky.util.LineReader;
import org.apache.commons.collections4.CollectionUtils;

import java.nio.file.Paths;
import java.util.*;

import static com.xkyss.mocky.contant.MockConsts.DICT_PATH;

public class Dicts implements MockUnit<String> {

    private final static String EXT = ".txt";

    private final Random random;

    private Map<String, StringDict> dicts = new HashMap<>();

    public Dicts(Random random) {
        this.random = random;
    }

    @Override
    public String get() {
        return null;
    }

    public String get(String key) {
        if (dicts.containsKey(key)) {
            return dicts.get(key).get();
        }

        String path = Paths.get(key).isAbsolute() ? key : (DICT_PATH + "/" + key + EXT);
        StringDict sd = new StringDict(random, path);
        dicts.put(key, sd);
        return sd.get();
    }

    class StringDict extends ALineDictUnit {

        private final String path;

        public StringDict(Random random, String path) {
            super(random);
            this.path = path;
        }

        @Override
        protected String path() {
            return this.path;
        }
    }
}
