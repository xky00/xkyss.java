package com.xkyss.quarkus.server;

import com.xkyss.quarkus.server.config.BuildConfig;
import io.quarkus.hibernate.validator.runtime.ValidatorProvider;
import io.quarkus.runtime.annotations.Recorder;
import jakarta.validation.MessageInterpolator;
import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.resourceloading.PlatformResourceBundleLocator;

import java.util.function.Supplier;

@Recorder
public class ServerRecorder {

    private final BuildConfig buildConfig;

    public ServerRecorder(BuildConfig buildConfig) {
        this.buildConfig = buildConfig;
    }

    public Supplier<MessageInterpolator> messageInterpolatorSupplier() {
        return () -> new ResourceBundleMessageInterpolator(
            new PlatformResourceBundleLocator(buildConfig.validationMessagesPath()));
    }
}
