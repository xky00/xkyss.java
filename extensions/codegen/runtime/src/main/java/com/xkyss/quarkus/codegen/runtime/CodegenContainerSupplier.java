package com.xkyss.quarkus.codegen.runtime;

import com.xkyss.core.util.Listx;
import com.xkyss.experimental.naming.Converter;
import org.jboss.logging.Logger;

import java.util.*;
import java.util.function.Supplier;

public class CodegenContainerSupplier implements Supplier<CodegenContainerSupplier.Container> {

    static Container INSTANCE = new Container();

    private static final Logger log = Logger.getLogger(CodegenContainerSupplier.class);

    @Override
    public Container get() {
        return INSTANCE;
    }

    public static void initWith(CodegenConfig config) {
        if (config == null) {
            log.warn("config is NULL.");
            return;
        }

        INSTANCE.clear();

        if (!Listx.isNullOrEmpty(config.source())) {
            for (CodegenConfig.SourceConfig c: config.source()) {
                INSTANCE.sources.put(c.name(), c);
            }
        }

        if (!Listx.isNullOrEmpty(config.target())) {
            for (CodegenConfig.TargetConfig c: config.target()) {
                INSTANCE.targets.put(c.name(), new TargetConfigProxy(c));
            }
        }

        if (!Listx.isNullOrEmpty(config.generate())) {
            INSTANCE.generators.addAll(config.generate());
        }
        log.infof("Source: %d, Target: %d, Generators: %d",
            INSTANCE.sources.size(), INSTANCE.targets.size(), INSTANCE.generators.size());
    }

    public static class TargetConfigProxy implements CodegenConfig.TargetConfig {
        private CodegenConfig.TargetConfig config;

        TargetConfigProxy(CodegenConfig.TargetConfig config) {
            this.config = Objects.requireNonNull(config);
        }

        @Override
        public String name() {
            return config.name();
        }

        @Override
        public Optional<String> template() {
            return config.template().isPresent() ? config.template() : Optional.of(config.name());
        }

        @Override
        public Optional<String> relativePackage() {
            return config.relativePackage().isPresent() ? config.relativePackage() : Optional.of(config.name());
        }

        @Override
        public Optional<String> postfix() {
            return config.postfix().isPresent()
                ? config.postfix()
                : Optional.of(Converter.fromAuto(config.name()).toPascal());
        }

        @Override
        public String fileExt() {
            return config.fileExt();
        }

        @Override
        public Optional<List<String>> dependencies() {
            return config.dependencies();
        }
    }

    public static class Container {
        public Map<String, CodegenConfig.SourceConfig> sources = new HashMap<>();

        public Map<String, CodegenConfig.TargetConfig> targets = new HashMap<>();

        public List<CodegenConfig.GenerateConfig> generators = new ArrayList<>();

        public void clear() {
            sources.clear();
            targets.clear();
            generators.clear();
        }
    }
}
