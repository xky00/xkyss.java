package com.xkyss.quarkus.codegen.runtime;

import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;
import io.smallrye.config.ConfigMapping;

import java.util.List;
import java.util.Optional;


@ConfigMapping(prefix = "xkyss.codegen")
@ConfigRoot(phase = ConfigPhase.BUILD_AND_RUN_TIME_FIXED)
public interface CodegenConfig {

    /**
     * Data source of code generator
     */
    List<SourceConfig> source();

    /**
     * Data target of code generator
     */
    List<TargetConfig> target();

    /**
     * Generators
     */
    List<GenerateConfig> generate();


    interface SourceConfig {

        /**
         * Source name
         */
        String name();

        /**
         * DataSource name
         */
        String dsName();

        /**
         * Source kind:
         *  db / sql / insert-sql / json
         */
        String kind();
    }

    interface TargetConfig {

        /**
         * Target name
         */
        String name();

        /**
         * Template name (qute)
         */
        Optional<String> template();

        /**
         * relative package
         */
        Optional<String> relativePackage();

        /**
         * postfix
         */
        Optional<String> postfix();

        /**
         * output file extension
         */
        String fileExt();

        /**
         * targets which depends on
         */
        Optional<List<String>> dependencies();
    }

    interface GenerateConfig {
        /**
         * Source name
         */
        String source();

        /**
         * Target name
         */
        List<String> target();

        /**
         * Package name
         */
        String packageName();
    }
}
