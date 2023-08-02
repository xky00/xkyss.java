package com.xkyss.quarkus.constant;

import com.xkyss.quarkus.server.KsServerRecorder;
import io.quarkus.arc.deployment.SyntheticBeanBuildItem;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.ExecutionTime;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Singleton;
import jakarta.validation.MessageInterpolator;
import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.resourceloading.PlatformResourceBundleLocator;
import org.hibernate.validator.spi.resourceloading.ResourceBundleLocator;
import org.jboss.logging.Logger;

public class ServerProcessor {

    private static final Logger LOGGER = Logger.getLogger(ServerProcessor.class.getName());

    private static final String FEATURE = "xkyss-server";

    @BuildStep
    FeatureBuildItem feature() {
        LOGGER.info(FEATURE);

        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    @Record(ExecutionTime.STATIC_INIT)
    SyntheticBeanBuildItem registerMessageInterpolator(KsServerRecorder ksServerRecorder) {
        return SyntheticBeanBuildItem.configure(MessageInterpolator.class)
            .scope(Singleton.class)
            .supplier(ksServerRecorder.messageInterpolatorSupplier())
            .done();
    }
}
