package com.xkyss.quarkus.extension.codebe.deployment;

import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;

class CodebeProcessor {

    private static final String FEATURE = "xkyss-quarkus-codebe";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

}
