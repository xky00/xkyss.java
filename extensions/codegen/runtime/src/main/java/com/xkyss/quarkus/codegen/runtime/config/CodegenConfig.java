package com.xkyss.quarkus.codegen.runtime.config;

import io.quarkus.runtime.annotations.ConfigDocSection;
import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigRoot;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@ConfigRoot(name = "codegen", prefix = "xkyss")
public class CodegenConfig {

    /**
     * Data source of code generator
     */
    @ConfigItem
    @ConfigDocSection
    public Map<String, SourceConfig> source = Collections.emptyMap();

    /**
     * Data target of code generator
     */
    @ConfigItem
    @ConfigDocSection
    public Map<String, TargetConfig> target = Collections.emptyMap();

    /**
     * Generators
     */
    @ConfigItem
    @ConfigDocSection
    public List<String> generate = List.of();
}
