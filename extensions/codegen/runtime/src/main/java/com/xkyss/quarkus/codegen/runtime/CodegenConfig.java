package com.xkyss.quarkus.codegen.runtime;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;

@ConfigGroup
public final class CodegenConfig {

    public static CodegenConfig defaultConfig() {
        return new CodegenConfig();
    }

    /**
     * 包名
     */
    @ConfigItem
    public String packageName;

    /**
     * entity 源目录
     */
    @ConfigItem(defaultValue = "entity")
    public String entityDir;

    /**
     * repository 生成目录
     */
    @ConfigItem(defaultValue = "repository")
    public String repositoryDir;

    /**
     * resource 生成目录
     */
    @ConfigItem(defaultValue = "resource")
    public String resourceDir;
}
