package com.xkyss.quarkus.codegen.runtime.config;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;

@ConfigGroup
public class GenerateConfig {
    /**
     * Source name
     */
    @ConfigItem
    public SourceConfig source;

    /**
     * Target name
     */
    @ConfigItem
    public TargetConfig target;
}
