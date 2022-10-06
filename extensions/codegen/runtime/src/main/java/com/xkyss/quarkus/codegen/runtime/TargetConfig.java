package com.xkyss.quarkus.codegen.runtime;

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

    /**
     * Relative package to source base package
     * separator: '.'
     */
    @ConfigItem
    public String relative;

    /**
     * Post fix for target file
     */
    @ConfigItem
    public String postfix;

    public static TargetConfig defaultConfig() {
        return new TargetConfig();
    }
}
