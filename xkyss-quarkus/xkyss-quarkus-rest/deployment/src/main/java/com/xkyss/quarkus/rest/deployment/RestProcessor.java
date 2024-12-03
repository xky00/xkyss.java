package com.xkyss.quarkus.rest.deployment;

import com.xkyss.quarkus.rest.runtime.RestRecorder;
import io.quarkus.arc.deployment.SyntheticBeanBuildItem;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.ExecutionTime;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import jakarta.inject.Singleton;
import jakarta.validation.MessageInterpolator;
import org.jboss.logging.Logger;

public class RestProcessor {

    private static final Logger LOGGER = Logger.getLogger(RestProcessor.class.getName());

    private static final String FEATURE = "xkyss-rest";

    @BuildStep
    FeatureBuildItem feature() {
        LOGGER.info(FEATURE);

        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    @Record(ExecutionTime.STATIC_INIT)
    SyntheticBeanBuildItem registerMessageInterpolator(RestRecorder ksServerRecorder) {
        return SyntheticBeanBuildItem.configure(MessageInterpolator.class)
            .scope(Singleton.class)
            .supplier(ksServerRecorder.messageInterpolatorSupplier())
            .done();
    }
}
