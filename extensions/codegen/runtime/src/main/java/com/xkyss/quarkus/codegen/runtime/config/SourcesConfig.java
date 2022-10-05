package com.xkyss.quarkus.codegen.runtime.config;

import io.quarkus.datasource.common.runtime.DataSourceUtil;
import io.quarkus.runtime.annotations.ConfigDocMapKey;
import io.quarkus.runtime.annotations.ConfigDocSection;
import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigRoot;

import java.util.*;

@ConfigRoot(name = "source", prefix = "xkyss.codegen")
public class SourcesConfig {

    /**
     * The default config
     */
    @ConfigItem(name = ConfigItem.PARENT)
    public SourceConfig defaultConfig = SourceConfig.defaultConfig();

    /**
     * Additional named config.
     */
    @ConfigDocSection
    @ConfigDocMapKey("codegen-source")
    @ConfigItem(name = ConfigItem.PARENT)
    public Map<String, SourceConfig> namedConfigs = Collections.emptyMap();

    public SourceConfig getConfig(String name) {
        if (DataSourceUtil.isDefault(name)) {
            return defaultConfig;
        }
        return namedConfigs.getOrDefault(name, SourceConfig.defaultConfig());
    }

    public Map<String, SourceConfig> getConfigs() {
        Map<String, SourceConfig> map = new HashMap<>(namedConfigs.size() + 1);
        map.put(DataSourceUtil.DEFAULT_DATASOURCE_NAME, defaultConfig);
        map.putAll(namedConfigs);
        return map;
    }
}
