package com.xkyss.quarkus.rest.config;

import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

import static com.xkyss.quarkus.rest.constant.Constants.CONFIG_BUILD_REST_PREFIX;
import static com.xkyss.quarkus.rest.constant.Constants.I18N_VALIDATION;

@ConfigMapping(prefix = CONFIG_BUILD_REST_PREFIX)
@ConfigRoot(phase = ConfigPhase.BUILD_AND_RUN_TIME_FIXED)
public interface BuildConfig {
    /**
     * 校验消息配置
     */
    @WithDefault(I18N_VALIDATION)
    String validationMessagesPath();

    /**
     * [ResponseFilter]配置
     */
    ResponseFilterConfig responseFilter();

    /**
     * [HttpLogFilterConfig]配置
     */
    HttpLogFilterConfig httpLogFilter();

    /**
     * [ExceptionMapper]配置
     */
    ExceptionMapperConfig exceptionMapper();


    interface ResponseFilterConfig {

        /**
         * 是否启用[ResponseFilter]
         */
        @WithDefault("false")
        boolean enabled();
    }

    interface HttpLogFilterConfig {
        /**
         * 是否启用[HttpLogFilter]
         */
        @WithDefault("false")
        boolean enabled();
    }

    interface ExceptionMapperConfig {
        /**
         * 是否启用[ExceptionMapper]
         */
        @WithDefault("false")
        boolean enabled();
    }
}