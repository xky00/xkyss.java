package com.xkyss.quarkus.codegen.runtime.config;

import java.util.Collection;
import java.util.Map;
import java.util.TreeSet;
import java.util.function.Supplier;

public class ConfigContainerSupplier implements Supplier<Collection<ConfigContainer>> {

    TreeSet<ConfigContainer> containers = new TreeSet<>(ConfigContainerComparator.INSTANCE);

    public ConfigContainerSupplier(SourcesConfig configs) {
        Map<String, SourceConfig> map = configs.getConfigs();
        for (Map.Entry<String, SourceConfig> entry : map.entrySet()) {
            containers.add(new ConfigContainer(entry.getKey(), entry.getValue()));
        }
    }

    @Override
    public Collection<ConfigContainer> get() {
        return containers;
    }
}
