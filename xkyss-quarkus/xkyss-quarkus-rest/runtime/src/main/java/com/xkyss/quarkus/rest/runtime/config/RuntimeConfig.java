package com.xkyss.quarkus.rest.runtime.config;

import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithConverter;
import io.smallrye.config.WithDefault;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.xkyss.quarkus.rest.constant.Constants.CONFIG_REST_PREFIX;


@ConfigMapping(prefix = CONFIG_REST_PREFIX)
@ConfigRoot(phase = ConfigPhase.RUN_TIME)
public interface RuntimeConfig {

    /**
     * App name
     * @return app name
     */
    String appName();

    /**
     * App sn
     * @return app sn
     */
    String appSn();

    /**
     * App version
     * @return app version
     */
    String appVersion();

    /**
     * Response filter config
     * @return config
     */
    Map<String, ResponseFilterConfig> responseFilter();

    /**
     * Http log filter config
     * @return config
     */
    Map<String, HttpLogFilterConfig> httpLogFilter();

    /**
     * Response filter config
     */
    interface ResponseFilterConfig {

        /**
         * Enable or disable
         * @return enable or disable
         */
        @WithDefault("true")
        boolean enabled();

        /**
         * Path pattern
         * @return path pattern
         */
        @WithDefault("/*")
        @WithConverter(LowerConverter.class)
        Optional<String> path();

        /**
         * Http methods
         * @return http methods
         */
        @WithDefault("*")
        @WithConverter(UpperListConverter.class)
        Optional<List<String>> methods();
    }

    /**
     * Http log filter config
     */
    interface HttpLogFilterConfig {

        /**
         * Enable or disable
         * @return enable or disable
         */
        @WithDefault("true")
        boolean enabled();

        /**
         * Path pattern
         * @return path pattern
         */
        @WithDefault("/*")
        @WithConverter(LowerConverter.class)
        Optional<String> path();

        /**
         * Http methods
         * @return http methods
         */
        @WithDefault("*")
        @WithConverter(UpperListConverter.class)
        Optional<List<String>> methods();
    }
}
