package com.xkyss.quarkus.codegen.runtime;

import com.xkyss.core.util.Listx;
import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class CodegenContainerSupplier implements Supplier<CodegenContainerSupplier.Container> {

    private static Container INSTANCE = new Container();

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

        if (!Listx.isNullOrEmpty(config.source())) {
            for (CodegenConfig.SourceConfig c: config.source()) {
                INSTANCE.sources.put(c.name(), c);
            }
        }
        log.infof("Source size: %d", INSTANCE.sources.size());

        if (!Listx.isNullOrEmpty(config.target())) {
            for (CodegenConfig.TargetConfig c: config.target()) {
                INSTANCE.targets.put(c.name(), c);
            }
        }
        log.infof("Target size: %d", INSTANCE.targets.size());

        if (!Listx.isNullOrEmpty(config.generate())) {
            INSTANCE.gens.addAll(config.generate());
        }
        log.infof("Gens size: %d", INSTANCE.gens.size());
    }


    public static class Container {
        public Map<String, CodegenConfig.SourceConfig> sources = new HashMap<>();

        public Map<String, CodegenConfig.TargetConfig> targets = new HashMap<>();

        public List<CodegenConfig.GenerateConfig> gens = new ArrayList<>();
    }
}
