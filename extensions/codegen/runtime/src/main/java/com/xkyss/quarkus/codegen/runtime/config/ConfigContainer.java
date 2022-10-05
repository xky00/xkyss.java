package com.xkyss.quarkus.codegen.runtime.config;

public class ConfigContainer {
    private final String id;
    private final SourceConfig config;

    public ConfigContainer(String id, SourceConfig config) {
        this.id = id;
        this.config = config;
    }

    public String getId() {
        return id;
    }

    public SourceConfig getConfig() {
        return config;
    }
}
