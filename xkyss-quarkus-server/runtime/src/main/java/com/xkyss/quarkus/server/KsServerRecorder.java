package com.xkyss.quarkus.server;

import com.xkyss.quarkus.server.config.KsBuildConfig;
import io.quarkus.runtime.annotations.Recorder;
import jakarta.validation.MessageInterpolator;
import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.resourceloading.PlatformResourceBundleLocator;

import java.util.function.Supplier;

@Recorder
public class KsServerRecorder {

    private final KsBuildConfig buildConfig;

    public KsServerRecorder(KsBuildConfig buildConfig) {
        this.buildConfig = buildConfig;
    }

    public Supplier<MessageInterpolator> messageInterpolatorSupplier() {
        return () -> new ResourceBundleMessageInterpolator(
            new PlatformResourceBundleLocator(buildConfig.validationMessagesPath()));
    }
}
