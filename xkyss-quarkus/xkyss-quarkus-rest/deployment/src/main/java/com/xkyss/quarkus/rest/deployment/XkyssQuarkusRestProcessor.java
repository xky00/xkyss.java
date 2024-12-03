package com.xkyss.quarkus.rest.deployment;

import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;

class XkyssQuarkusRestProcessor {

    private static final String FEATURE = "xkyss-quarkus-rest";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }
}
