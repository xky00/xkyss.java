package com.xkyss.quarkus.codegen.runtime.config;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;

@ConfigGroup
public class SourceConfig {

    /**
     * 包名
     */
    @ConfigItem
    public String packageName;

    public static SourceConfig defaultConfig() {
        return new SourceConfig();
    }
}
