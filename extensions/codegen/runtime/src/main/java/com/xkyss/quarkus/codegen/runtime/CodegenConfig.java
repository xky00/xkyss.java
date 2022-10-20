package com.xkyss.quarkus.codegen.runtime;

import com.xkyss.core.util.Stringx;
import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;
import io.smallrye.config.ConfigMapping;

import java.util.List;


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

        /**
         * Package name
         */
        String packageName();
    }

    interface TargetConfig {

        /**
         * Target name
         */
        String name();

        /**
         * Template name (qute)
         */
        default String template() {
            return name();
        }

        /**
         * relative package
         */
        default String relativePackage() {
            return name();
        }

        /**
         * postfix
         */
        default String postfix() {
            return Stringx.capitalize(name());
        }

        /**
         * output file extension
         */
        String fileExt();
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
