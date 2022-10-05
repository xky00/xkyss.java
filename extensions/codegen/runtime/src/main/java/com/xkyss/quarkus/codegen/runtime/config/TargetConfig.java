package com.xkyss.quarkus.codegen.runtime.config;

import io.quarkus.runtime.annotations.ConfigGroup;

@ConfigGroup
public class TargetConfig {
    public static TargetConfig defaultConfig() {
        return new TargetConfig();
    }
}
