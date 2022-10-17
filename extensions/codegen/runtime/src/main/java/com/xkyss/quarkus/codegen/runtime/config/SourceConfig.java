package com.xkyss.quarkus.codegen.runtime.config;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;

@ConfigGroup
public class SourceConfig {

    /**
     * Source name
     */
    @ConfigItem
    public String name;

    /**
     * Source kind:
     *  db / sql / insert-sql / json
     */
    @ConfigItem
    public String kind;
}
