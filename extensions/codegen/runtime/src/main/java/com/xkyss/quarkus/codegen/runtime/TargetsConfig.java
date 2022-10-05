package com.xkyss.quarkus.codegen.runtime;

import io.quarkus.datasource.common.runtime.DataSourceUtil;
import io.quarkus.runtime.annotations.ConfigDocMapKey;
import io.quarkus.runtime.annotations.ConfigDocSection;
import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigRoot;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@ConfigRoot(name = "target", prefix = "xkyss.codegen")
public class TargetsConfig {

    /**
     * The default config
     */
    @ConfigItem(name = ConfigItem.PARENT)
    public TargetConfig defaultConfig = TargetConfig.defaultConfig();

    /**
     * Additional named config.
     */
    @ConfigDocSection
    @ConfigDocMapKey("codegen-target")
    @ConfigItem(name = ConfigItem.PARENT)
    public Map<String, TargetConfig> namedConfigs = Collections.emptyMap();

    public TargetConfig getConfig(String name) {
        if (DataSourceUtil.isDefault(name)) {
            return defaultConfig;
        }
        return namedConfigs.getOrDefault(name, TargetConfig.defaultConfig());
    }

    public Map<String, TargetConfig> getConfigs() {
        Map<String, TargetConfig> map = new HashMap<>(namedConfigs.size() + 1);
        map.put(DataSourceUtil.DEFAULT_DATASOURCE_NAME, defaultConfig);
        map.putAll(namedConfigs);
        return map;
    }
}
