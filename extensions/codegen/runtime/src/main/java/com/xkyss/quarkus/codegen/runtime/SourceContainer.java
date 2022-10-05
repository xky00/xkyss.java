package com.xkyss.quarkus.codegen.runtime;

public class SourceContainer {

    private final String id;

    private final SourceConfig config;

    public SourceContainer(String id, SourceConfig config) {
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
