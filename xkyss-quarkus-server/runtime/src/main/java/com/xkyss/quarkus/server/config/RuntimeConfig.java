package com.xkyss.quarkus.server.config;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

import java.util.Optional;
import java.util.Set;

import static com.xkyss.quarkus.server.constant.Constants.CONFIG_SERVER_PREFIX;


/**
 * 配置
 */
@ConfigMapping(prefix = CONFIG_SERVER_PREFIX)
public interface RuntimeConfig {

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

    /**
     * 是否开启http日志
     */
    @WithDefault("true")
    boolean httpLogEnable();

    /**
     * 需要额外配置的路由
     * 由@Route定义的路由,默认无法由route()拦截,在这里配置
     */
    Optional<Set<String>> httpLogRoutes();
}
