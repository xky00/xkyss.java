package com.xkyss.quarkus.codegen.runtime.config;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;

@ConfigGroup
public class TargetConfig {

    /**
     * Target name
     */
    @ConfigItem
    public String name;

    /**
     * Template name (qute)
     */
    @ConfigItem
    public String template;
}
