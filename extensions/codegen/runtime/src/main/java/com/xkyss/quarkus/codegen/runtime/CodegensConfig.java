package com.xkyss.quarkus.codegen.runtime;

import io.quarkus.datasource.common.runtime.DataSourceUtil;
import io.quarkus.runtime.annotations.*;

import java.util.Collections;
import java.util.Map;

@ConfigRoot(name = "codegen", prefix = "xkyss")
public class CodegensConfig {

    /**
     * The default config
     */
    @ConfigItem(name = ConfigItem.PARENT)
    public CodegenConfig defaultConfig = CodegenConfig.defaultConfig();

    /**
     * Additional named config.
     */
    @ConfigDocSection
    @ConfigDocMapKey("codegen-name")
    @ConfigItem(name = ConfigItem.PARENT)
    public Map<String, CodegenConfig> namedConfigs = Collections.emptyMap();

    public CodegenConfig getConfig(String name) {
        if (DataSourceUtil.isDefault(name)) {
            return defaultConfig;
        }
        return namedConfigs.getOrDefault(name, CodegenConfig.defaultConfig());
    }
}
