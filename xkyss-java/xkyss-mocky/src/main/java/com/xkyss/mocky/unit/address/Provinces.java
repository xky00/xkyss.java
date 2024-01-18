package com.xkyss.mocky.unit.address;

import com.xkyss.mocky.abstraction.ACachedDictUnit;
import com.xkyss.mocky.abstraction.ACsvDictUnit;
import com.xkyss.mocky.model.Province;
import org.apache.commons.csv.CSVRecord;

import java.nio.file.Paths;
import java.util.Random;

/**
 * 省
 */
public class Provinces extends ACsvDictUnit<Province> {

    public Provinces(Random random) {
        super(random);
    }

    @Override
    protected String path() {
        return Paths.get("dicts", "address", "province.csv").toString();
    }

    @Override
    protected Province convert(CSVRecord record) {
        Province p = new Province();
        p.setCode(record.get(0));
        p.setName(record.get(1));
        p.setShortName(record.get(2));
        return p;
    }
}
