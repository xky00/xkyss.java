package com.xkyss.quarkus.codegen.runtime;

public class CodegenContainer {

    private final String id;
    private final CodegenConfig config;

    public CodegenContainer(String id, CodegenConfig config) {
        this.id = id;
        this.config = config;
    }

    public String getId() {
        return id;
    }

    public CodegenConfig getConfig() {
        return config;
    }
}
