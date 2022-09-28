package com.xkyss.quarkus.extension.codegen.deployment;

import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;

class XkyssQuarkusExtensionCodegenProcessor {

    private static final String FEATURE = "xkyss-quarkus-extension-codegen";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }
}
