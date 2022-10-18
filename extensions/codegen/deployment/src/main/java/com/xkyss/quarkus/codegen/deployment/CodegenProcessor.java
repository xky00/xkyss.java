package com.xkyss.quarkus.codegen.deployment;

import com.xkyss.quarkus.codegen.runtime.config.CodegenConfig;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import org.jboss.logging.Logger;

class CodegenProcessor {

    private static final Logger log = Logger.getLogger(CodegenProcessor.class);
    private static final String FEATURE = "xkyss-quarkus-codegen";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }
}
