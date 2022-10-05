package com.xkyss.quarkus.codegen.runtime.config;

import io.quarkus.datasource.common.runtime.DataSourceUtil;
import java.util.Comparator;

public class ConfigContainerComparator implements Comparator<ConfigContainer> {

    public static final ConfigContainerComparator INSTANCE = new ConfigContainerComparator();

    @Override
    public int compare(ConfigContainer o1, ConfigContainer o2) {
        String id1 = o1.getId();
        String id2 = o2.getId();

        if (DataSourceUtil.isDefault(id1)) {
            return -1;
        }
        if (DataSourceUtil.isDefault(id2)) {
            return 1;
        }

        return id1.compareTo(id2);
    }
}
