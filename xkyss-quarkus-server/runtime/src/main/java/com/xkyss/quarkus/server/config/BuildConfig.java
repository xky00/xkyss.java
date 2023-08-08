package com.xkyss.quarkus.server.config;

import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

import java.util.Set;

import static com.xkyss.quarkus.server.constant.Constants.CONFIG_SERVER_BUILD_PREFIX;
import static com.xkyss.quarkus.server.constant.Constants.I18N_VALIDATION;

@ConfigMapping(prefix = CONFIG_SERVER_BUILD_PREFIX)
@ConfigRoot(phase = ConfigPhase.BUILD_AND_RUN_TIME_FIXED)
public interface BuildConfig {
    /**
     * 校验消息配置
     */
    @WithDefault(I18N_VALIDATION)
    String validationMessagesPath();

    /**
     * ResponseFilter配置
     */
    ResponseFilterConfig responseFilter();

    interface ResponseFilterConfig {

        /**
         * 是否启用ResponseFilter
         */
        @WithDefault("false")
        boolean enabled();

        /**
         * 不进行Response包装的类型
         */
        Set<String> ignoreTypes();

    }
}
