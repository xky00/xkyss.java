package com.xkyss.quarkus.codegen.runtime;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;

@ConfigGroup
public class SourceConfig {

    /**
     * Package name (base, parent of Entity)
     */
    @ConfigItem
    public String packageName;

    public static SourceConfig defaultConfig() {
        return new SourceConfig();
    }
}
