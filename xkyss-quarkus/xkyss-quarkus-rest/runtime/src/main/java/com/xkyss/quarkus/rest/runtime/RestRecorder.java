package com.xkyss.quarkus.rest.runtime;

import com.xkyss.quarkus.rest.runtime.config.BuildConfig;
import io.quarkus.runtime.annotations.Recorder;
import jakarta.validation.MessageInterpolator;
import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.resourceloading.PlatformResourceBundleLocator;

import java.util.function.Supplier;

@Recorder
public class RestRecorder {

    private final BuildConfig buildConfig;

    public RestRecorder(BuildConfig buildConfig) {
        this.buildConfig = buildConfig;
    }

    public Supplier<MessageInterpolator> messageInterpolatorSupplier() {
        return () -> new ResourceBundleMessageInterpolator(
            new PlatformResourceBundleLocator(buildConfig.validationMessagesPath()));
    }
}
