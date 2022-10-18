package com.xkyss.quarkus.codegen.runtime.config;

import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;
import io.smallrye.config.ConfigMapping;

import java.util.List;
import java.util.Optional;
import java.util.Set;


@ConfigMapping(prefix = "server")
@ConfigRoot(phase = ConfigPhase.BUILD_AND_RUN_TIME_FIXED)
public interface ServerCollections {
    /**
     * e
     */
    Set<Environment> environments();

    interface Environment {
        /**
         * name
         */
        String name();

        /**
         * apps
         */
        List<App> apps();

        /**
         * App
         */
        interface App {
            /**
             * name
             */
            String name();

            /**
             * services
             */
            List<String> services();

            /**
             * database
             */
            Optional<List<String>> databases();
        }
    }
}
