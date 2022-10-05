package com.xkyss.quarkus.codegen.runtime;

public class TargetContainer {
    private final String id;

    private final TargetConfig config;

    public TargetContainer(String id, TargetConfig config) {
        this.id = id;
        this.config = config;
    }

    public String getId() {
        return id;
    }

    public TargetConfig getCOnfig() {
        return config;
    }
}
