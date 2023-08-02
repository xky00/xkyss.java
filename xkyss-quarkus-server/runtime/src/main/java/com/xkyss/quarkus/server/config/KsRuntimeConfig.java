package com.xkyss.quarkus.server.config;

import io.smallrye.config.ConfigMapping;

import static com.xkyss.quarkus.server.constant.KsConstants.CONFIG_PREFIX;


/**
 * 配置
 */
@ConfigMapping(prefix = CONFIG_PREFIX)
public interface KsRuntimeConfig {

    /**
     * 项目名称
     */


    String appName();

    /**
     * 项目版本
     */
    String appVersion();

    /**
     * 版权
     */
    String copyright();
}
