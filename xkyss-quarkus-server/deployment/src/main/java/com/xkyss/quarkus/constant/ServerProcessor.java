package com.xkyss.quarkus.constant;

import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import org.jboss.logging.Logger;

public class ServerProcessor {

    private static final Logger LOGGER = Logger.getLogger(ServerProcessor.class.getName());

    private static final String FEATURE = "xkyss-server";

    @BuildStep
    FeatureBuildItem feature() {
        LOGGER.info(FEATURE);

        return new FeatureBuildItem(FEATURE);
    }
}
