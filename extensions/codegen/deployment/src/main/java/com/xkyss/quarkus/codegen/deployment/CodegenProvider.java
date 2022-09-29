package com.xkyss.quarkus.codegen.deployment;

import io.quarkus.bootstrap.prebuild.CodeGenException;
import io.quarkus.deployment.CodeGenContext;
import io.quarkus.deployment.CodeGenProvider;
import org.eclipse.microprofile.config.Config;
import org.jboss.logging.Logger;

import java.nio.file.Path;

public class CodegenProvider implements CodeGenProvider {

    private static final Logger log = Logger.getLogger(CodegenProvider.class);

    @Override
    public String providerId() {
        return "xkyss-codegen-provider-id";
    }

    @Override
    public String inputExtension() {
        return "xkyss-codegen-input-extension";
    }

    @Override
    public String inputDirectory() {
        return "xkyss-codegen-input-directory";
    }

    @Override
    public boolean shouldRun(Path sourceDir, Config config) {
        log.info("shouldRun");
        return true;
    }

    @Override
    public boolean trigger(CodeGenContext context) throws CodeGenException {
        log.info("trigger: context:");
        log.infof("inputDir: %s", context.inputDir());
        log.infof("outDir: %s", context.outDir());
        log.infof("workDir: %s", context.workDir());
        log.infof("", context.config().getOptionalValue("quarkus.datasource.username", String.class));
        return true;
    }
}
