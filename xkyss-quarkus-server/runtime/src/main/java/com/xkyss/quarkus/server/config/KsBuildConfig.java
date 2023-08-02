package com.xkyss.quarkus.server.config;

import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

import static com.xkyss.quarkus.server.constant.KsConstants.CONFIG_BUILD_PREFIX;

@ConfigMapping(prefix = CONFIG_BUILD_PREFIX)
@ConfigRoot(phase = ConfigPhase.BUILD_AND_RUN_TIME_FIXED)
public interface KsBuildConfig {
    /**
     * 校验消息配置
     */
    @WithDefault("i18n/validation")
    String validationMessagesPath();
}
