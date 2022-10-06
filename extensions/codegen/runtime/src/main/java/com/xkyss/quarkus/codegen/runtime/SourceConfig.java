package com.xkyss.quarkus.codegen.runtime;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;

@ConfigGroup
public class SourceConfig {

    /**
     * Package name
     * eg: com.package.name.of.entity
     */
    @ConfigItem
    public String packageName;

    /**
     * Relative depth to package-name path (source base path)
     * eg: 1 for "com.package.name.of.entity" is "com.package.name.of"
     *     2 for "com.package.name.of.entity" is "com.package.name"
     */
    @ConfigItem(defaultValue = "1")
    public Integer relativeDepth;

    public static SourceConfig defaultConfig() {
        return new SourceConfig();
    }

    public String getRelativePackage() {
        if (packageName == null || packageName.length() == 0) {
            return packageName;
        }
        if (relativeDepth == null || relativeDepth == 0) {
            return packageName;
        }

        String ret = packageName;
        for (int i = 0; i < relativeDepth; i++) {
            int dotIndex = ret.lastIndexOf('.');
            if (dotIndex <= 0) {
                break;
            }
            ret = ret.substring(0, dotIndex);
        }

        return ret;
    }
}
